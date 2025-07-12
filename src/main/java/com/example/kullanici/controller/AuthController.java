package com.example.kullanici.controller;

import com.example.kullanici.model.AuthRequest;
import com.example.kullanici.model.AuthResponse;
import com.example.kullanici.model.Kullanici;
import com.example.kullanici.repository.KullaniciRepository;
import com.example.kullanici.security.JwtUtil;
import com.example.kullanici.security.TokenBlacklist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;
    private final KullaniciRepository kullaniciRepository;

    public AuthController(JwtUtil jwtUtil, TokenBlacklist tokenBlacklist, KullaniciRepository kullaniciRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenBlacklist = tokenBlacklist;
        this.kullaniciRepository = kullaniciRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<Kullanici> optionalKullanici = kullaniciRepository.findByKullaniciAdi(request.getKullaniciAdi());

        if (optionalKullanici.isPresent()) {
            Kullanici kullanici = optionalKullanici.get();
            if (kullanici.getSifre().equals(request.getSifre())) {
                String token = jwtUtil.generateToken(kullanici.getKullaniciAdi());
                return ResponseEntity.ok(new AuthResponse(token));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz kullanıcı adı veya şifre");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                tokenBlacklist.blacklistToken(token);
                return ResponseEntity.ok("Logout başarılı");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz token");
            }
        }
        return ResponseEntity.badRequest().body("Token header bulunamadı");
    }
}
