package com.example.kullanici.service;

import com.example.kullanici.model.Kullanici;
import com.example.kullanici.repository.KullaniciRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class KullaniciService {

    private final KullaniciRepository repository;

    public KullaniciService(KullaniciRepository repository) {
        this.repository = repository;
    }

    public Kullanici kaydet(Kullanici kullanici) {
        return repository.save(kullanici);
    }
    public Optional<Kullanici> girisKontrol(String kullaniciAdi, String sifre) {
        return repository.findByKullaniciAdiAndSifre(kullaniciAdi, sifre);
    }


}
