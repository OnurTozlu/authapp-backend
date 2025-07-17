package com.example.kullanici.model;

import java.util.HashMap;
import java.util.Map;

public enum ArkadaslikDurumu {
    BEKLIYOR(0),
    KABUL_EDILDI(1),
    REDDEDILDI(2);

    private final int kod;

    private static final Map<Integer, ArkadaslikDurumu> kodToEnum = new HashMap<>();

    static {
        for (ArkadaslikDurumu durum : ArkadaslikDurumu.values()) {
            kodToEnum.put(durum.kod, durum);
        }
    }

    ArkadaslikDurumu(int kod) {
        this.kod = kod;
    }

    public int getKod() {
        return kod;
    }

    public static ArkadaslikDurumu fromKod(int kod) {
        ArkadaslikDurumu durum = kodToEnum.get(kod);
        if (durum == null) {
            throw new IllegalArgumentException("Geçersiz durum kodu: " + kod);
        }
        return durum;
    }
}
