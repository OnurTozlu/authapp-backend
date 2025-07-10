package com.example.kullanici.repository;

import com.example.kullanici.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KullaniciRepository extends JpaRepository<Kullanici, Long> {
}
