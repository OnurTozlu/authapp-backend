package com.example.kullanici.DTO;

public class MesajDTO {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String timestamp;
    private String senderIsim;
    private String senderSoyisim;
    private String receiverIsim;    // Yeni alan
    private String receiverSoyisim; // Yeni alan

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderIsim() {
        return senderIsim;
    }

    public void setSenderIsim(String senderIsim) {
        this.senderIsim = senderIsim;
    }

    public String getSenderSoyisim() {
        return senderSoyisim;
    }

    public void setSenderSoyisim(String senderSoyisim) {
        this.senderSoyisim = senderSoyisim;
    }

    public String getReceiverIsim() {
        return receiverIsim;
    }

    public void setReceiverIsim(String receiverIsim) {
        this.receiverIsim = receiverIsim;
    }

    public String getReceiverSoyisim() {
        return receiverSoyisim;
    }

    public void setReceiverSoyisim(String receiverSoyisim) {
        this.receiverSoyisim = receiverSoyisim;
    }
}
