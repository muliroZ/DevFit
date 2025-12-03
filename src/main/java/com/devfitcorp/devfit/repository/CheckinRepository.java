package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {

    @Query(value = "SELECT HOUR(c.dataHora) as hora, COUNT(c) as count " +
            "FROM Checkin c " +
            "WHERE DATE(c.dataHora) = :data " +
            "GROUP BY hora " +
            "ORDER BY hora ASC")
    List<Object[]> findCheckinCountByHour(@Param("data") LocalDate data);

    List<Checkin> findTop20ByOrderByIdDesc();

    long countByDataHoraBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    boolean existsByUsuarioIdAndDataHoraBetween(Long usuarioId, LocalDateTime dezMinutosAtras, LocalDateTime agora);
}
