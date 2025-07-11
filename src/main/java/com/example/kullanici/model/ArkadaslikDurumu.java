package com.example.kullanici.model;

public enum ArkadaslikDurumu {
    BEKLIYOR(0),
    KABUL_EDILDI(1),
    REDDEDILDI(2);

    private final int kod;

    ArkadaslikDurumu(int kod) {
        this.kod = kod;
    }

    public int getKod() {
        return kod;
    }

    public static ArkadaslikDurumu fromKod(int kod) {
        for (ArkadaslikDurumu durum : values()) {
            if (durum.kod == kod) return durum;
        }
        throw new IllegalArgumentException("Geçersiz durum kodu: " + kod);
    }
}
