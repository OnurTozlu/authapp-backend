package com.example.kullanici.model;

import jakarta.persistence.*;

@Entity
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String kullaniciAdi;

    @Column(length = 100, nullable = false)
    private String sifre;

    @Column(length = 50, nullable = false)
    private String isim;

    @Column(length = 20, nullable = false)
    private String soyisim;

    @Column(length = 100, nullable = false, unique = true)
    private String mail;

    @Column(length = 10, nullable = false, unique = true)
    private Integer numara;


    // Getter ve Setter'lar


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getSoyisim() {
        return soyisim;
    }

    public void setSoyisim(String soyisim) {
        this.soyisim = soyisim;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getNumara() {
        return numara;
    }

    public void setNumara(Integer numara) {
        this.numara = numara;
    }
}
