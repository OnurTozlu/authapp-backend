package com.example.kullanici.security;

import com.example.kullanici.model.Kullanici;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Kullanici kullanici;

    public CustomUserDetails(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Yetki yönetimi eklenecekse burada yapabilirsiniz, şu an boş liste dönüyor.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return kullanici.getSifre();
    }

    @Override
    public String getUsername() {
        return kullanici.getKullaniciAdi();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
