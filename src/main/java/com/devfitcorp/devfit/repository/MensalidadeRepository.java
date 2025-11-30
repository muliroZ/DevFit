package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Mensalidade;
import jakarta.persistence.criteria.From;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long>{

    @Query("SELECT COALESCE(SUM(m.valorPago), 0) FROM Mensalidade m " +
            "WHERE m.dataPagamento BETWEEN :dataInicio AND :dataFim")

    BigDecimal sumValorPagoByPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                     @Param("dataFim") LocalDate dataFim);

}
