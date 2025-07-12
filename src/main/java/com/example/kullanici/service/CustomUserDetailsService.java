package com.example.kullanici.service;

import com.example.kullanici.model.Kullanici;
import com.example.kullanici.repository.KullaniciRepository;
import com.example.kullanici.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final KullaniciRepository kullaniciRepository;

    public CustomUserDetailsService(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String kullaniciAdi) throws UsernameNotFoundException {
        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(kullaniciAdi)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + kullaniciAdi));

        return new CustomUserDetails(kullanici);
    }
}
