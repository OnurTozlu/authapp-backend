package com.example.kullanici.controller;

import java.util.Optional;
import com.example.kullanici.model.Kullanici;
import com.example.kullanici.service.KullaniciService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/kullanici")
@CrossOrigin(origins = "http://localhost:3000")  // Frontend portunu kendi durumuna göre ayarla
public class KullaniciController {

    private final KullaniciService service;

    public KullaniciController(KullaniciService service) {
        this.service = service;
    }

    @PostMapping("/kayit")
    public Kullanici kullaniciKaydet(@RequestBody Kullanici kullanici) {
        return service.kaydet(kullanici);
    }

    @PostMapping("/giris")
    public ResponseEntity<?> girisYap(@RequestBody Kullanici kullanici) {
        Optional<Kullanici> bulunanKullanici = service.girisKontrol(kullanici.getKullaniciAdi(), kullanici.getSifre());
        if (bulunanKullanici.isPresent()) {
            return ResponseEntity.ok(bulunanKullanici.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Kullanıcı adı veya şifre hatalı");
        }
    }


}
