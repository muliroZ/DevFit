// src/main/java/com/devfitcorp/devfit/mappers/DashboardMapper.java

package com.devfitcorp.devfit.mappers;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.devfitcorp.devfit.dto.AdminStatsDTO;
import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;

@Component
public class DashboardMapper {

    public AdminStatsDTO toResponse(HashMap<String, Object> stats) {

        Long totalAlunosOn = ((Number) stats.get("totalAlunosOn")).longValue();
        Long totalAlunosOff = ((Number) stats.get("totalAlunosOff")).longValue();
        Long totalAlunos = ((Number) stats.get("totalAlunos")).longValue();
        Long manutencaoEquip = ((Number) stats.get("manutencaoEquip")).longValue();
        Long totalEquip = ((Number) stats.get("totalEquip")).longValue();
        Long checkinsHoje = ((Number) stats.get("checkinsHoje")).longValue();

        return new AdminStatsDTO(
                (BigDecimal) stats.get("faturamentoMensalPrevisto"),
                totalAlunosOn,
                totalAlunosOff,
                (Double) stats.get("taxaRetencao"),
                totalAlunos,
                (Integer) stats.get("capacidadeMax"),
                manutencaoEquip,
                totalEquip,
                checkinsHoje,
                (FinanceiroDashboardDTO) stats.get("financeiro")
        );
    }
}