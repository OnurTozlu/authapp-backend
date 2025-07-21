package com.example.kullanici.controller;

import com.example.kullanici.model.Kullanici;
import com.example.kullanici.model.Mesaj;
import com.example.kullanici.repository.KullaniciRepository;
import com.example.kullanici.repository.MesajRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/mesajlar")
@CrossOrigin(origins = "http://localhost:3000")
public class MesajController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MesajRepository mesajRepository;
    private final KullaniciRepository kullaniciRepository;

    @Autowired
    public MesajController(SimpMessagingTemplate messagingTemplate,
                           MesajRepository mesajRepository,
                           KullaniciRepository kullaniciRepository) {
        this.messagingTemplate = messagingTemplate;
        this.mesajRepository = mesajRepository;
        this.kullaniciRepository = kullaniciRepository;
    }

    @PostMapping
    public Mesaj mesajGonder(@RequestBody Mesaj mesaj) {
        Kullanici sender = kullaniciRepository.findById(mesaj.getSender().getId())
                .orElseThrow(() -> new IllegalArgumentException("Gönderen bulunamadı"));
        Kullanici receiver = kullaniciRepository.findById(mesaj.getReceiver().getId())
                .orElseThrow(() -> new IllegalArgumentException("Alıcı bulunamadı"));

        mesaj.setSender(sender);
        mesaj.setReceiver(receiver);
        mesaj.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        Mesaj saved = mesajRepository.save(mesaj);

        // Alıcıya özel WebSocket mesajı gönder
        messagingTemplate.convertAndSendToUser(
                String.valueOf(saved.getReceiver().getId()),
                "/queue/mesajlar",
                saved
        );

        return saved;
    }

    @GetMapping
    public List<Mesaj> mesajlariGetir(Authentication authentication, @RequestParam Long aliciId) {
        String username = authentication.getName();

        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        Long kullaniciId = kullanici.getId();

        return mesajRepository.findChatMessagesBetweenUsers(kullaniciId, aliciId);
    }
}
