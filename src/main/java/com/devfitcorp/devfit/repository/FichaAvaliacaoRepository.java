package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.FichaAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface FichaAvaliacaoRepository  extends JpaRepository<FichaAvaliacao, Long> {


    List<FichaAvaliacao> FindByAlunoId(Long alunoId);
}
