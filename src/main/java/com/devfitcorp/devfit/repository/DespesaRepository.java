package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM Despesa d " +
            "WHERE d.categoria = :categoria AND d.dataVencimento BETWEEN :dataInicio AND :dataFim")
    BigDecimal sumValorByCategoriaAndPeriodo(@Param("categoria") String categoria,
                                             @Param("dataInicio") LocalDate dataInicio,
                                             @Param("dataFim") LocalDate dataFim);
}
