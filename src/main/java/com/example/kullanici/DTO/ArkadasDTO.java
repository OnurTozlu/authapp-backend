package com.example.kullanici.DTO;

public class ArkadasDTO {
    private Long kullaniciId;
    private String kullaniciAdi;
    private String profilFotoUrl;

    public ArkadasDTO(Long kullaniciId, String kullaniciAdi, String profilFotoUrl) {
        this.kullaniciId = kullaniciId;
        this.kullaniciAdi = kullaniciAdi;
        this.profilFotoUrl = profilFotoUrl;
    }

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

    public String getProfilFotoUrl() {
        return profilFotoUrl;
    }

    public void setProfilFotoUrl(String profilFotoUrl) {
        this.profilFotoUrl = profilFotoUrl;
    }

}
