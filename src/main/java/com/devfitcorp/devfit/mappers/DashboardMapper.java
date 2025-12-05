package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.AdminStatsDTO;
import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

@Component
public class DashboardMapper {

    public AdminStatsDTO toResponse(HashMap<String, Object> stats) {
        return new AdminStatsDTO(
                (BigDecimal) stats.get("faturamentoMP"),
                (Long) stats.get("totalAlunosOn"),
                (Long) stats.get("totalAlunosOff"),
                (Double) stats.get("taxaRetencao"),
                (Long) stats.get("totalUsers"),
                (Integer) stats.get("capacidadeMax"),
                (Long) stats.get("manutencaoEquip"),
                (Long) stats.get("totalEquip"),
                (Long) stats.get("checkinsHoje"),
                (FinanceiroDashboardDTO) stats.get("financeiro")
        );
    }
}
