package com.example.kullanici.DTO;

// com.example.kullanici.DTO.ArkadasDTO
public class ArkadasDTO {
    private Long kullaniciId;
    private String kullaniciAdi;

    public ArkadasDTO(Long kullaniciId, String kullaniciAdi) {
        this.kullaniciId = kullaniciId;
        this.kullaniciAdi = kullaniciAdi;
    }

    // getter-setter metodları

    public Long getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(Long kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }
}

