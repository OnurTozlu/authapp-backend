package com.example.kullanici.controller;

import com.example.kullanici.model.Kullanici;
import com.example.kullanici.security.JwtUtil;
import com.example.kullanici.service.KullaniciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/kullanici")
@CrossOrigin(origins = "http://localhost:3000")  // React için CORS
public class KullaniciController {

    private final KullaniciService service;

    @Autowired
    private JwtUtil jwtUtil;

    public KullaniciController(KullaniciService service) {
        this.service = service;
    }

    @PostMapping("/kayit")
    public Kullanici kullaniciKaydet(@RequestBody Kullanici kullanici) {
        return service.kaydet(kullanici);
    }

    // ✅ Giriş endpoint (JWT ile)
    @PostMapping("/giris")
    public ResponseEntity<?> girisYap(@RequestBody Kullanici kullanici) {
        var bulunanKullaniciOpt = service.girisKontrol(kullanici.getKullaniciAdi(), kullanici.getSifre());
        if (bulunanKullaniciOpt.isPresent()) {
            Kullanici user = bulunanKullaniciOpt.get();
            String token = jwtUtil.generateToken(user.getKullaniciAdi(), user.getId());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "kullanici", user
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Kullanıcı adı veya şifre hatalı");
        }
    }

    // ✅ Kullanıcı bilgisi getiren endpoint (/me)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token yok veya hatalı");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz token");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            Kullanici user = service.kullaniciGetir(userId);

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token hatalı");
        }
    }
}
