package com.devfitcorp.devfit.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
@Setter
public class AdminStatsDTO {


    private BigDecimal faturamentoMensalPrevisto;
    private long totalAlunosAtivos;
    private long totalAlunosInativos;

    private double taxaRetencao;
    private long totalUsuariosCadastrados;
    private int capacidadeMaxima;
    private long equipamentosEmManutencao;
    private long equipamentosTotais;
    private long checkinsHoje;

}