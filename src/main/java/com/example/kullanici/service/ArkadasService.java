package com.example.kullanici.service;

import com.example.kullanici.model.Arkadas;
import com.example.kullanici.model.ArkadaslikDurumu;
import com.example.kullanici.model.Kullanici;
import com.example.kullanici.repository.ArkadasRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArkadasService {

    private final ArkadasRepository arkadasRepository;

    public ArkadasService(ArkadasRepository arkadasRepository) {
        this.arkadasRepository = arkadasRepository;
    }

    public Arkadas arkadaslikIsteğiGonder(Kullanici gonderen, Kullanici alan) {
        Arkadas arkadas = new Arkadas();
        arkadas.setIstekGonderen(gonderen);
        arkadas.setIstekAlan(alan);
        arkadas.setDurum(ArkadaslikDurumu.BEKLIYOR);
        return arkadasRepository.save(arkadas);
    }

    public List<Arkadas> kullanicininTumArkadasliklari(Kullanici kullanici) {
        return arkadasRepository.findByIstekAlanOrIstekGonderen(kullanici, kullanici);
    }

    public void arkadaslikDurumunuGuncelle(Long arkadasId, ArkadaslikDurumu yeniDurum) {
        Arkadas arkadas = arkadasRepository.findById(arkadasId).orElseThrow();
        arkadas.setDurum(yeniDurum);
        arkadasRepository.save(arkadas);
    }
}
