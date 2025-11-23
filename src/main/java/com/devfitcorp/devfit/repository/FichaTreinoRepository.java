package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.FichaTreino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Estende JpaRepository: ele fornece todos os metodos CRUD (save, findById, findAll, delete, etc)
@Repository
public interface FichaTreinoRepository extends JpaRepository<FichaTreino, Long> {
    // Metodo personalizado para buscar fichas de treino por ID do aluno
    List<FichaTreino> findByAlunoId(Long alunoId);
}
