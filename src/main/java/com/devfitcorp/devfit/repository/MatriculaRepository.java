package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
}