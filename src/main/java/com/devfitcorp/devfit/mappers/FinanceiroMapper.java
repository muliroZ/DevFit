package com.devfitcorp.devfit.mappers;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;

@Component
public class FinanceiroMapper {
    
    public FinanceiroDashboardDTO toResponse(
        BigDecimal receitaTotal, 
        BigDecimal despesaTotal, 
        BigDecimal lucroLiquido, 
        HashMap<String, BigDecimal> receitaMensalidades, 
        HashMap<String, BigDecimal> receitaProdutos
    ) {
        return new FinanceiroDashboardDTO(
            receitaTotal, 
            despesaTotal, 
            lucroLiquido, 
            receitaMensalidades, 
            receitaProdutos
        );
    }
}
