package com.example.kullanici.service;

import com.example.kullanici.model.Kullanici;
import com.example.kullanici.repository.KullaniciRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
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

    public Kullanici kullaniciGetir(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + id));
    }

    public Optional<Kullanici> kullaniciBul(String kullaniciAdi) {
        return repository.findByKullaniciAdi(kullaniciAdi);
    }

    public Kullanici kullaniciGetirByKullaniciAdi(String kullaniciAdi) {
        return repository.findByKullaniciAdi(kullaniciAdi)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + kullaniciAdi));
    }

    public boolean kullaniciVarMi(Long id) {
        return repository.existsById(id);
    }

    // ✅ Kullanıcı Güncelleme Metodu
    public Kullanici kullaniciGuncelle(Long id, Map<String, Object> veri) {
        Kullanici mevcut = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (veri.containsKey("isim") && veri.get("isim") instanceof String) {
            mevcut.setIsim((String) veri.get("isim"));
        }
        if (veri.containsKey("soyisim") && veri.get("soyisim") instanceof String) {
            mevcut.setSoyisim((String) veri.get("soyisim"));
        }
        if (veri.containsKey("kullaniciAdi") && veri.get("kullaniciAdi") instanceof String) {
            mevcut.setKullaniciAdi((String) veri.get("kullaniciAdi"));
        }
        if (veri.containsKey("mail") && veri.get("mail") instanceof String) {
            mevcut.setMail((String) veri.get("mail"));
        }
        if (veri.containsKey("profilFotoUrl") && veri.get("profilFotoUrl") instanceof String) {
            mevcut.setProfilFotoUrl((String) veri.get("profilFotoUrl"));
        }

        // Şifre boş değilse ve String ise güncelle
        if (veri.containsKey("sifre") && veri.get("sifre") instanceof String) {
            String sifre = ((String) veri.get("sifre")).trim();
            if (!sifre.isEmpty()) {
                // TODO: Şifreyi burada hash'leyin. Şimdilik direkt atılıyor.
                mevcut.setSifre(sifre);
            }
        }

        return repository.save(mevcut);
    }
}
