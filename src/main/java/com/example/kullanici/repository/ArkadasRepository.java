package com.example.kullanici.repository;

import com.example.kullanici.model.Arkadas;
import com.example.kullanici.model.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArkadasRepository extends JpaRepository<Arkadas, Long> {

    List<Arkadas> findByIstekAlanOrIstekGonderen(Kullanici alan, Kullanici gonderen);

    List<Arkadas> findByIstekAlanAndDurumKod(Kullanici alan, int durumKod);

    List<Arkadas> findByIstekGonderenAndDurumKod(Kullanici gonderen, int durumKod);
}
