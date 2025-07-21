package com.example.kullanici.controller;

import com.example.kullanici.DTO.ArkadasDTO;
import com.example.kullanici.DTO.ArkadasIstekDTO;
import com.example.kullanici.model.Arkadas;
import com.example.kullanici.model.ArkadaslikDurumu;
import com.example.kullanici.model.Kullanici;
import com.example.kullanici.service.ArkadasService;
import com.example.kullanici.service.KullaniciService;
import com.example.kullanici.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/arkadas")
public class ArkadasController {

    private final ArkadasService arkadasService;
    private final KullaniciService kullaniciService;
    private final JwtUtil jwtUtil;

    public ArkadasController(ArkadasService arkadasService, KullaniciService kullaniciService, JwtUtil jwtUtil) {
        this.arkadasService = arkadasService;
        this.kullaniciService = kullaniciService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/istek")
    public Arkadas arkadasIsteğiGonder(@RequestParam Long gonderenId, @RequestParam Long alanId) {
        Kullanici gonderen = kullaniciService.kullaniciGetir(gonderenId);
        Kullanici alan = kullaniciService.kullaniciGetir(alanId);
        return arkadasService.arkadaslikIsteğiGonder(gonderen, alan);
    }

    @PostMapping
    public ArkadasDTO arkadasEkle(@RequestBody Map<String, String> body, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long gonderenId = jwtUtil.getUserIdFromToken(token);

        String arkadasAdi = body.get("kullaniciAdi");

        Kullanici gonderen = kullaniciService.kullaniciGetir(gonderenId);
        Kullanici alan = kullaniciService.kullaniciGetirByKullaniciAdi(arkadasAdi);

        arkadasService.arkadaslikIsteğiGonder(gonderen, alan);

        return new ArkadasDTO(alan.getId(), alan.getKullaniciAdi(), alan.getProfilFotoUrl());
    }

    @PutMapping("/{id}/durum")
    public void arkadaslikDurumuGuncelle(@PathVariable Long id, @RequestParam int durum) {
        ArkadaslikDurumu yeniDurum = ArkadaslikDurumu.fromKod(durum);
        arkadasService.arkadaslikDurumunuGuncelle(id, yeniDurum);
    }

    @GetMapping("/liste")
    public List<ArkadasDTO> arkadasliklariListele(@RequestParam Long kullaniciId) {
        Kullanici kullanici = kullaniciService.kullaniciGetir(kullaniciId);
        List<Arkadas> arkadasliklar = arkadasService.kullanicininTumArkadasliklari(kullanici);

        return arkadasliklar.stream()
                .filter(a -> a.getDurum() == ArkadaslikDurumu.KABUL_EDILDI)
                .map(a -> {
                    Kullanici diger = a.getIstekGonderen().getId().equals(kullaniciId)
                            ? a.getIstekAlan()
                            : a.getIstekGonderen();
                    return new ArkadasDTO(diger.getId(), diger.getKullaniciAdi(), diger.getProfilFotoUrl());
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/istekler")
    public List<ArkadasIstekDTO> bekleyenArkadaslikIstekleri(@RequestParam Long alanId) {
        Kullanici kullanici = kullaniciService.kullaniciGetir(alanId);
        List<Arkadas> istekler = arkadasService.bekleyenIstekleriGetir(kullanici);

        return istekler.stream()
                .map(istek -> {
                    Kullanici gonderen = istek.getIstekGonderen();
                    String isimSoyisim = gonderen.getIsim() + " " + gonderen.getSoyisim();
                    return new ArkadasIstekDTO(
                            istek.getId(),
                            gonderen.getKullaniciAdi(),
                            isimSoyisim
                    );
                })
                .collect(Collectors.toList());
    }

}
