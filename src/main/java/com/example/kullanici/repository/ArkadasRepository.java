package com.example.kullanici.repository;

import com.example.kullanici.DTO.ArkadasIstekDTO;
import com.example.kullanici.model.Arkadas;
import com.example.kullanici.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArkadasRepository extends JpaRepository<Arkadas, Long> {

    List<Arkadas> findByIstekAlanOrIstekGonderen(Kullanici alan, Kullanici gonderen);
    List<Arkadas> findByIstekAlanAndDurumKod(Kullanici alan, int durumKod);
    List<Arkadas> findByIstekGonderenAndDurumKod(Kullanici gonderen, int durumKod);

    boolean existsByIstekGonderenAndIstekAlanAndDurumKodIn(Kullanici gonderen, Kullanici alan, List<Integer> durumlar);

    boolean existsByIstekGonderenAndIstekAlanOrIstekGonderenAndIstekAlanAndDurumKodIn(
            Kullanici gonderen1, Kullanici alan1,
            Kullanici gonderen2, Kullanici alan2,
            List<Integer> durumlar
    );

    // ✅ Yeni: Bekleyen istekleri DTO olarak getir
    @Query("SELECT new com.example.kullanici.DTO.ArkadasIstekDTO(" +
            "a.id, g.kullaniciAdi, CONCAT(g.isim, ' ', g.soyisim)) " +
            "FROM Arkadas a " +
            "JOIN a.istekGonderen g " +
            "WHERE a.istekAlan.id = :kullaniciId AND a.durumKod = 0")
    List<ArkadasIstekDTO> findBekleyenIstekler(@Param("kullaniciId") Long kullaniciId);
}
