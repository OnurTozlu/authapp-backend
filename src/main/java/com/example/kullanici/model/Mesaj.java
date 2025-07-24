package com.example.kullanici.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "mesajlar")
public class Mesaj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private Kullanici sender;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonIgnore
    private Kullanici receiver;


    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);

    // JSON’a sadece temel alanları ekle
    @JsonProperty("id")
    public Long getJsonId() {
        return id;
    }

    @JsonProperty("senderId")
    public Long getJsonSenderId() {
        return sender != null ? sender.getId() : null;
    }

    @JsonProperty("receiverId")
    public Long getJsonReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }

    @JsonProperty("content")
    public String getJsonContent() {
        return content;
    }

    @JsonProperty("timestamp")
    public String getJsonTimestamp() {
        return createdAt != null ? createdAt.toString() : null;
    }

    // Standart getter-setterlar
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Kullanici getSender() {
        return sender;
    }
    public void setSender(Kullanici sender) {
        this.sender = sender;
    }

    public Kullanici getReceiver() {
        return receiver;
    }
    public void setReceiver(Kullanici receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
