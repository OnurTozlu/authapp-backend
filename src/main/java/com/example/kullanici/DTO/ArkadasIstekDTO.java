package com.example.kullanici.DTO;

public class ArkadasIstekDTO {
    private Long istekId;
    private String gonderenKullaniciAdi;
    private String gonderenIsimSoyisim;

    public ArkadasIstekDTO(Long istekId, String gonderenKullaniciAdi, String gonderenIsimSoyisim) {
        this.istekId = istekId;
        this.gonderenKullaniciAdi = gonderenKullaniciAdi;
        this.gonderenIsimSoyisim = gonderenIsimSoyisim;
    }

    public Long getIstekId() { return istekId; }
    public void setIstekId(Long istekId) { this.istekId = istekId; }

    public String getGonderenKullaniciAdi() { return gonderenKullaniciAdi; }
    public void setGonderenKullaniciAdi(String gonderenKullaniciAdi) { this.gonderenKullaniciAdi = gonderenKullaniciAdi; }

    public String getGonderenIsimSoyisim() { return gonderenIsimSoyisim; }
    public void setGonderenIsimSoyisim(String gonderenIsimSoyisim) { this.gonderenIsimSoyisim = gonderenIsimSoyisim; }
}
