package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanoRepository extends JpaRepository<Plano, Long> {
    List<Plano> findAllByAtivo(boolean ativo);
}