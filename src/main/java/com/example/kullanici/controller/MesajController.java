package com.example.kullanici.controller;

import com.example.kullanici.DTO.MesajDTO;
import com.example.kullanici.model.Kullanici;
import com.example.kullanici.model.Mesaj;
import com.example.kullanici.repository.KullaniciRepository;
import com.example.kullanici.repository.MesajRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

    // ✅ REST ile mesaj gönderme (Fallback için)
    @PostMapping
    public Mesaj mesajGonder(@RequestBody Mesaj mesaj) {
        return mesajKaydetVeGonder(mesaj);
    }

    // ✅ WebSocket ile mesaj gönderme (Ana kanal)
    @MessageMapping("/mesaj-gonder")
    public void mesajGonderWS(@Payload MesajDTO mesajDto, SimpMessageHeaderAccessor headerAccessor) {
        if (mesajDto.getSenderId() == null) {
            System.err.println("Hata: Gönderen ID eksik");
            return;
        }
        if (mesajDto.getReceiverId() == null) {
            System.err.println("Hata: Alıcı ID eksik");
            return;
        }

        Kullanici sender = kullaniciRepository.findById(mesajDto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Gönderen bulunamadı"));
        Kullanici receiver = kullaniciRepository.findById(mesajDto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Alıcı bulunamadı"));

        Mesaj mesaj = new Mesaj();
        mesaj.setSender(sender);
        mesaj.setReceiver(receiver);
        mesaj.setContent(mesajDto.getContent());
        mesaj.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        Mesaj saved = mesajRepository.save(mesaj);

        System.out.println("WebSocket mesajı alındı: " + mesaj.getContent());
        System.out.println("Gönderen ID: " + sender.getId());
        System.out.println("Alıcı ID: " + receiver.getId());

        messagingTemplate.convertAndSendToUser(
                String.valueOf(saved.getReceiver().getId()),
                "/queue/mesajlar",
                saved
        );
    }



    // 🔁 Ortak mesaj kaydetme ve gönderme metodu
    private Mesaj mesajKaydetVeGonder(Mesaj mesaj) {
        Kullanici sender = kullaniciRepository.findById(mesaj.getSender().getId())
                .orElseThrow(() -> new IllegalArgumentException("Gönderen bulunamadı"));
        Kullanici receiver = kullaniciRepository.findById(mesaj.getReceiver().getId())
                .orElseThrow(() -> new IllegalArgumentException("Alıcı bulunamadı"));

        mesaj.setSender(sender);
        mesaj.setReceiver(receiver);
        mesaj.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        Mesaj saved = mesajRepository.save(mesaj);

        // WebSocket üzerinden alıcıya gönder
        messagingTemplate.convertAndSendToUser(
                String.valueOf(saved.getReceiver().getId()),
                "/queue/mesajlar",
                saved
        );

        return saved;
    }

    // ✅ Mesaj geçmişi alma
    @GetMapping
    public List<Mesaj> mesajlariGetir(Authentication authentication, @RequestParam Long aliciId) {
        String username = authentication.getName();

        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        return mesajRepository.findChatMessagesBetweenUsers(kullanici.getId(), aliciId);
    }
}
