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

    // Kullanıcıyı kaydeder
    public Kullanici kaydet(Kullanici kullanici) {
        return repository.save(kullanici);
    }

    // Kullanıcı adı ve şifreyle giriş kontrolü yapar
    public Optional<Kullanici> girisKontrol(String kullaniciAdi, String sifre) {
        return repository.findByKullaniciAdiAndSifre(kullaniciAdi, sifre);
    }

    // ID ile kullanıcıyı getirir
    public Kullanici kullaniciGetir(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + id));
    }

    // Kullanıcı adına göre kullanıcıyı getirir (Optional olarak)
    public Optional<Kullanici> kullaniciBul(String kullaniciAdi) {
        return repository.findByKullaniciAdi(kullaniciAdi);
    }

    // Kullanıcı adına göre kullanıcıyı getirir (Exception fırlatırsa)
    public Kullanici kullaniciGetirByKullaniciAdi(String kullaniciAdi) {
        return repository.findByKullaniciAdi(kullaniciAdi)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + kullaniciAdi));
    }

    // Belirtilen ID'ye sahip kullanıcı var mı kontrol eder
    public boolean kullaniciVarMi(Long id) {
        return repository.existsById(id);
    }
}
