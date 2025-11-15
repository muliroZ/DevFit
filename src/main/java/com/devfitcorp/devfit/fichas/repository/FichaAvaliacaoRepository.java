package com.devfitcorp.devfit.fichas.repository;

import com.devfitcorp.devfit.fichas.model.FichaAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface FichaAvaliacaoRepository  extends JpaRepository<FichaAvaliacao, Long> {


    List<FichaAvaliacao> FindByAlunoId(Long alunoId);
}
