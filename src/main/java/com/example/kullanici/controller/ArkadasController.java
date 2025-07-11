package com.example.kullanici.controller;

import com.example.kullanici.model.Arkadas;
import com.example.kullanici.model.ArkadaslikDurumu;
import com.example.kullanici.model.Kullanici;
import com.example.kullanici.service.ArkadasService;
import com.example.kullanici.service.KullaniciService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arkadas")
public class ArkadasController {

    private final ArkadasService arkadasService;
    private final KullaniciService kullaniciService;

    public ArkadasController(ArkadasService arkadasService, KullaniciService kullaniciService) {
        this.arkadasService = arkadasService;
        this.kullaniciService = kullaniciService;
    }

    @PostMapping("/istek")
    public Arkadas arkadasIsteğiGonder(@RequestParam Long gonderenId, @RequestParam Long alanId) {
        Kullanici gonderen = kullaniciService.kullaniciGetir(gonderenId);
        Kullanici alan = kullaniciService.kullaniciGetir(alanId);
        return arkadasService.arkadaslikIsteğiGonder(gonderen, alan);
    }

    @PutMapping("/{id}/durum")
    public void arkadaslikDurumuGuncelle(@PathVariable Long id, @RequestParam int durum) {
        arkadasService.arkadaslikDurumunuGuncelle(id, ArkadaslikDurumu.fromKod(durum));
    }

    @GetMapping("/liste")
    public List<Arkadas> arkadasliklariListele(@RequestParam Long kullaniciId) {
        Kullanici kullanici = kullaniciService.kullaniciGetir(kullaniciId);
        return arkadasService.kullanicininTumArkadasliklari(kullanici);
    }
}
