package com.example.kullanici.controller;

import com.example.kullanici.model.Kullanici;
import com.example.kullanici.security.JwtUtil;
import com.example.kullanici.service.KullaniciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/kullanici")
@CrossOrigin(origins = "http://localhost:3000")
public class KullaniciController {

    private final KullaniciService service;

    @Autowired
    private JwtUtil jwtUtil;

    public KullaniciController(KullaniciService service) {
        this.service = service;
    }

    @PostMapping("/kayit")
    public ResponseEntity<Kullanici> kullaniciKaydet(@RequestBody Kullanici kullanici) {
        Kullanici saved = service.kaydet(kullanici);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

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

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token hatalı");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> kullaniciGuncelle(
            @PathVariable Long id,
            @RequestBody Map<String, Object> guncelVeriler) {
        try {
            if (guncelVeriler.containsKey("sifre")) {
                Object sifreObj = guncelVeriler.get("sifre");
                if (sifreObj instanceof String && ((String) sifreObj).trim().isEmpty()) {
                    guncelVeriler.remove("sifre");
                }
            }

            Kullanici guncellenen = service.kullaniciGuncelle(id, guncelVeriler);
            return ResponseEntity.ok(guncellenen);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/uploadProfilFoto")
    public ResponseEntity<?> uploadProfilFoto(
            @PathVariable Long id,
            @RequestParam("profilFoto") MultipartFile file,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token yok veya hatalı");
            }
            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz token");
            }

            Long tokenUserId = jwtUtil.getUserIdFromToken(token);
            if (!tokenUserId.equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Başkasının profilini güncelleyemezsiniz");
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Dosya boş");
            }

            // Dosya adı oluştur
            String dosyaAdi = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // uploads klasörünü proje kökünde kullan, yoksa oluştur
            java.nio.file.Path yuklemeYolu = java.nio.file.Paths.get("uploads").toAbsolutePath().normalize();
            java.nio.file.Files.createDirectories(yuklemeYolu);

            // Dosya tam yolu
            java.nio.file.Path dosyaYolu = yuklemeYolu.resolve(dosyaAdi);

            // Dosyayı fiziksel olarak kaydet
            java.nio.file.Files.copy(file.getInputStream(), dosyaYolu);

            // Frontend erişimi için URL (statik kaynak olarak ayarlanmalı)
            String dosyaUrl = "/uploads/" + dosyaAdi;

            Kullanici kullanici = service.kullaniciGetir(id);
            kullanici.setProfilFotoUrl(dosyaUrl);
            service.kaydet(kullanici);

            return ResponseEntity.ok(Map.of("message", "Profil fotoğrafı yüklendi", "url", dosyaUrl));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu: " + e.getMessage());
        }
    }

}
