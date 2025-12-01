package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    @Query("SELECT COUNT(e) FROM Equipamento e WHERE e.status = :status")
    long countByStatus(@Param("status") String status);
}
