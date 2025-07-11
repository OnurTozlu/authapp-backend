package com.example.kullanici.controller;

import com.example.kullanici.model.Mesaj;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MesajController {

    @MessageMapping("/chat.sendMessage")  // Client’tan mesaj bu endpoint’e gönderilir
    @SendTo("/topic/public")               // Tüm abonelere yayınlanır
    public Mesaj sendMessage(Mesaj message) {
        // Mesajı kaydetmek istersen burada yapabilirsin
        return message;
    }
}
