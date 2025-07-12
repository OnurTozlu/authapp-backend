package com.example.kullanici.repository;

import com.example.kullanici.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {

    Optional<Kullanici> findByKullaniciAdi(String kullaniciAdi);

    // Bu satır eksikti — EKLEDİK:
    Optional<Kullanici> findByKullaniciAdiAndSifre(String kullaniciAdi, String sifre);
}
