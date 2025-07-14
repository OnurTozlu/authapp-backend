package com.example.kullanici.model;


public class LoginResponse {
    private String token;
    private Kullanici kullanici;

    public LoginResponse(String token, Kullanici kullanici) {
        this.token = token;
        this.kullanici = kullanici;
    }

    public String getToken() {
        return token;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }
}
