package com.example.kullanici.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Arkadas {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "istek_gonderen_id", nullable = false)
    private Kullanici istekGonderen;

    @ManyToOne
    @JoinColumn(name = "istek_alan_id", nullable = false)
    private Kullanici istekAlan;

    @Column(name = "durum", nullable = false)
    private int durumKod = 0; // varsayılan: BEKLIYOR

    private LocalDateTime istekTarihi = LocalDateTime.now();

    // GETTER & SETTER

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kullanici getIstekGonderen() {
        return istekGonderen;
    }

    public void setIstekGonderen(Kullanici istekGonderen) {
        this.istekGonderen = istekGonderen;
    }

    public Kullanici getIstekAlan() {
        return istekAlan;
    }

    public void setIstekAlan(Kullanici istekAlan) {
        this.istekAlan = istekAlan;
    }

    public LocalDateTime getIstekTarihi() {
        return istekTarihi;
    }

    public void setIstekTarihi(LocalDateTime istekTarihi) {
        this.istekTarihi = istekTarihi;
    }

    public ArkadaslikDurumu getDurum() {
        return ArkadaslikDurumu.fromKod(durumKod);
    }

    public void setDurum(ArkadaslikDurumu durum) {
        this.durumKod = durum.getKod();
    }

    public int getDurumKod() {
        return durumKod;
    }

    public void setDurumKod(int durumKod) {
        this.durumKod = durumKod;
    }
}
