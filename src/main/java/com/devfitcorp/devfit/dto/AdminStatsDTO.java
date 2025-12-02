package com.devfitcorp.devfit.dto;

import java.math.BigDecimal;

public record AdminStatsDTO (
        BigDecimal faturamentoMensalPrevisto,
        long totalAlunosAtivos,
        long totalAlunosInativos,
        double taxaRetencao,
        long totalUsuariosCadastrados,
        int capacidadeMaxima,
        long equipamentosEmManutencao,
        long equipamentosTotais,
        long checkinsHoje,
        FinanceiroDashboardDTO financeiroDashboardDTO
){}