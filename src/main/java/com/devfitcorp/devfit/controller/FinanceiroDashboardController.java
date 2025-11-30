package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;
import com.devfitcorp.devfit.service.FinanceiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/dashboard/financeiro")
public class FinanceiroDashboardController {

    private final FinanceiroService financeiroService;

    public FinanceiroDashboardController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/resumo")
    public ResponseEntity<FinanceiroDashboardDTO> getFinancialSummary() {

        FinanceiroDashboardDTO resumo = financeiroService.getAggregatedFinanceiroSummary();
        return ResponseEntity.ok(resumo);
    }
}
