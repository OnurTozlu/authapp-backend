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
import java.util.stream.Collectors;

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

    // REST ile mesaj gönderme (Fallback)
    @PostMapping
    public Mesaj mesajGonder(@RequestBody Mesaj mesaj) {
        return mesajKaydetVeGonder(mesaj);
    }

    // WebSocket ile mesaj gönderme
    @MessageMapping("/mesaj-gonder")
    public void mesajGonderWS(@Payload MesajDTO mesajDto, SimpMessageHeaderAccessor headerAccessor) {
        if (mesajDto.getSenderId() == null || mesajDto.getReceiverId() == null) {
            System.err.println("Hata: Gönderen veya Alıcı ID eksik");
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

        // DTO'ya isim-soyisim set et (Gönderen ve Alıcı)
        MesajDTO responseDto = toDTO(saved);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(saved.getReceiver().getId()),
                "/queue/mesajlar",
                responseDto
        );
    }

    // Mesaj kaydet ve WebSocket ile gönder
    private Mesaj mesajKaydetVeGonder(Mesaj mesaj) {
        Kullanici sender = kullaniciRepository.findById(mesaj.getSender().getId())
                .orElseThrow(() -> new IllegalArgumentException("Gönderen bulunamadı"));
        Kullanici receiver = kullaniciRepository.findById(mesaj.getReceiver().getId())
                .orElseThrow(() -> new IllegalArgumentException("Alıcı bulunamadı"));

        mesaj.setSender(sender);
        mesaj.setReceiver(receiver);
        mesaj.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        Mesaj saved = mesajRepository.save(mesaj);

        // WebSocket üzerinden alıcıya DTO olarak gönder
        messagingTemplate.convertAndSendToUser(
                String.valueOf(saved.getReceiver().getId()),
                "/queue/mesajlar",
                toDTO(saved)
        );

        return saved;
    }

    // Mesaj geçmişini DTO listesi olarak döndür
    @GetMapping
    public List<MesajDTO> mesajlariGetir(Authentication authentication, @RequestParam Long aliciId) {
        String username = authentication.getName();

        Kullanici kullanici = kullaniciRepository.findByKullaniciAdi(username)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        List<Mesaj> mesajlar = mesajRepository.findChatMessagesBetweenUsers(kullanici.getId(), aliciId);

        // Mesajları DTO’ya dönüştür
        return mesajlar.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MesajDTO toDTO(Mesaj mesaj) {
        MesajDTO dto = new MesajDTO();
        dto.setSenderId(mesaj.getSender().getId());
        dto.setReceiverId(mesaj.getReceiver().getId());
        dto.setContent(mesaj.getContent());
        dto.setTimestamp(mesaj.getCreatedAt().toString());

        dto.setSenderIsim(mesaj.getSender().getIsim());
        dto.setSenderSoyisim(mesaj.getSender().getSoyisim());

        // Yeni eklenen alana da set edelim
        dto.setReceiverIsim(mesaj.getReceiver().getIsim());
        dto.setReceiverSoyisim(mesaj.getReceiver().getSoyisim());

        return dto;
    }

}
