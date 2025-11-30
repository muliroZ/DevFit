package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Optional<Matricula> findTopByUsuarioIdAndIsAtivaTrueOrderByDataVencimentoDesc(Long usuarioId);

    boolean existsByUsuarioIdAndIsAtivaTrue(Long usuarioId);

    long countByIsAtiva(boolean isAtiva);
}