package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
}
