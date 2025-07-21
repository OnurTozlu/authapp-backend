package com.example.kullanici.service;

import com.example.kullanici.model.Mesaj;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MesajService {

    private final List<Mesaj> mesajlar = new CopyOnWriteArrayList<>();

    public Mesaj kaydetMesaj(Mesaj mesaj) {
        mesaj.setId((long) (mesajlar.size() + 1));
        mesaj.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        mesajlar.add(mesaj);
        return mesaj;
    }

    public List<Mesaj> getMesajlar(String gondericiId, String aliciId) {
        List<Mesaj> result = new ArrayList<>();
        for (Mesaj m : mesajlar) {
            if ((String.valueOf(m.getSender().getId()).equals(gondericiId) &&
                    String.valueOf(m.getReceiver().getId()).equals(aliciId)) ||
                    (String.valueOf(m.getSender().getId()).equals(aliciId) &&
                            String.valueOf(m.getReceiver().getId()).equals(gondericiId))) {
                result.add(m);
            }
        }
        return result;
    }
}
