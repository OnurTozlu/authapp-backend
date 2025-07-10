package com.example.kullanici.controller;

import com.example.kullanici.model.Kullanici;
import com.example.kullanici.service.KullaniciService;
import org.springframework.web.bind.annotation.*;

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
}
