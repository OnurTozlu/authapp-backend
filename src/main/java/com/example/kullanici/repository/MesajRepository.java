package com.example.kullanici.repository;

import com.example.kullanici.model.Mesaj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MesajRepository extends JpaRepository<Mesaj, Long> {

    @Query("SELECT m FROM Mesaj m " +
            "JOIN FETCH m.sender s " +
            "JOIN FETCH m.receiver r " +
            "WHERE (m.sender.id = :kullaniciId AND m.receiver.id = :aliciId) OR " +
            "(m.sender.id = :aliciId AND m.receiver.id = :kullaniciId) " +
            "ORDER BY m.createdAt")
    List<Mesaj> findChatMessagesBetweenUsers(
            @Param("kullaniciId") Long kullaniciId,
            @Param("aliciId") Long aliciId);


}
