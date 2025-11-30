package com.devfitcorp.devfit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinanceiroDashboardDTO {

    private BigDecimal receitaTotal;
    private BigDecimal despesaTotal;
    private BigDecimal lucroLiquido;

    private Map<String, BigDecimal> receitaPorFonte;

    private Map<String, BigDecimal> despesaPorCategoria;
}
