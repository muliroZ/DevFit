package com.devfitcorp.devfit.fichas.repository;


import com.devfitcorp.devfit.fichas.model.FichaTreino;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface FichaTreinoRepository {

    // Salvar/Atualizar: Cria ou modifica uma ficha.
    FichaTreino save(FichaTreino ficha);

    // Buscar por ID: Optional evita o erro NullPointerException se o ID não existir.
    Optional<FichaTreino> findById(Long id);

    // Buscar todos: Retorna a lista completa.
    List<FichaTreino> findAll();

    // Deletar: Remove a ficha com base no ID.
    void deleteById(Long id);

    // Busca customizada: Encontrar todas as fichas de um aluno.
    List<FichaTreino> findByAlunoId(Long alunoId);



}
