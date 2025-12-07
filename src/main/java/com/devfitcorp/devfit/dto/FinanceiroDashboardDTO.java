package com.devfitcorp.devfit.dto;

import java.util.Map;
import java.math.BigDecimal;

public record FinanceiroDashboardDTO(
        BigDecimal receitaTotal,
        BigDecimal despesaTotal,
        BigDecimal lucroLiquido,

        Map<String, BigDecimal> receitaPorFonte,
        Map<String, BigDecimal> despesaPorCategoria
) {}
