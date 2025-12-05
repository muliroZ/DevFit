package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query(
            "SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p " +
            "WHERE p.dataCriacao BETWEEN :dataInicio AND :dataFinal"
    )
    BigDecimal sumValorTotalByPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                      @Param("dataFinal") LocalDate dataFinal);
}