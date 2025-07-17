package com.example.kullanici.service;

import com.example.kullanici.model.Arkadas;
import com.example.kullanici.model.ArkadaslikDurumu;
import com.example.kullanici.model.Kullanici;
import com.example.kullanici.repository.ArkadasRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ArkadasService {

    private final ArkadasRepository arkadasRepository;

    public ArkadasService(ArkadasRepository arkadasRepository) {
        this.arkadasRepository = arkadasRepository;
    }

    // ✅ GÜNCEL — Arkadaşlık isteği gönder
    public Arkadas arkadaslikIsteğiGonder(Kullanici gonderen, Kullanici alan) {
        List<Integer> kontrolEdilecekDurumlar = Arrays.asList(
                ArkadaslikDurumu.BEKLIYOR.getKod(),
                ArkadaslikDurumu.KABUL_EDILDI.getKod()
        );

        boolean zatenIliskiVar = arkadasRepository
                .existsByIstekGonderenAndIstekAlanOrIstekGonderenAndIstekAlanAndDurumKodIn(
                        gonderen, alan, alan, gonderen, kontrolEdilecekDurumlar
                );

        if (zatenIliskiVar) {
            throw new IllegalStateException("Bu kullanıcıyla zaten arkadaşsınız veya isteğiniz beklemede.");
        }

        Arkadas arkadas = new Arkadas();
        arkadas.setIstekGonderen(gonderen);
        arkadas.setIstekAlan(alan);
        arkadas.setDurum(ArkadaslikDurumu.BEKLIYOR);
        return arkadasRepository.save(arkadas);
    }

    // Kullanıcının tüm arkadaşlıklarını getir (durumu KABUL_EDILDI olanlar)
    public List<Arkadas> kullanicininTumArkadasliklari(Kullanici kullanici) {
        return arkadasRepository.findByIstekAlanOrIstekGonderen(kullanici, kullanici);
    }

    // Arkadaşlık durumunu güncelle (KABUL, RED vs)
    public boolean arkadaslikDurumunuGuncelle(Long arkadasId, ArkadaslikDurumu yeniDurum) {
        return arkadasRepository.findById(arkadasId).map(arkadas -> {
            arkadas.setDurum(yeniDurum);
            arkadasRepository.save(arkadas);
            return true;
        }).orElse(false);
    }

    // Bekleyen (durum = BEKLIYOR) arkadaşlık isteklerini getir
    public List<Arkadas> bekleyenIstekleriGetir(Kullanici kullanici) {
        return arkadasRepository.findByIstekAlanAndDurumKod(kullanici, ArkadaslikDurumu.BEKLIYOR.getKod());
    }
}
