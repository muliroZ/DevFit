package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.AdminStatsDTO;
import com.devfitcorp.devfit.dto.CheckinStatsByHourDTO;
import com.devfitcorp.devfit.dto.UsuarioDetalhadoDashboardDTO;
import com.devfitcorp.devfit.service.AdminDashboardService;
import com.devfitcorp.devfit.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.time.LocalDate;


@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    private final UsuarioService usuarioService;
    private final AdminDashboardService adminDashboardService;


    public AdminDashboardController(UsuarioService usuarioService, AdminDashboardService adminDashboardService) {
        this.usuarioService = usuarioService;
        this.adminDashboardService = adminDashboardService;
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/usuarios/detalhado")
    public ResponseEntity<Map<String, List<UsuarioDetalhadoDashboardDTO>>> getUsersListDetalhado() {
        Map<String, List<UsuarioDetalhadoDashboardDTO>> usuariosAgrupados = usuarioService.findAndGroupByRole();
        Map<String, List<UsuarioDetalhadoDashboardDTO>> resultado = Optional.ofNullable(usuariosAgrupados)
                .orElse(Collections.emptyMap());

        return ResponseEntity.ok(usuariosAgrupados);
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDTO> getGeneralStats() {
        AdminStatsDTO stats = adminDashboardService.getGeneralStats();
        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/stats/picos")
    public ResponseEntity<List<CheckinStatsByHourDTO>> getPeakHoursStats(@RequestParam(required = false) String data) {

        LocalDate dataConsulta = data != null ? LocalDate.parse(data) : LocalDate.now();

        return ResponseEntity.ok(adminDashboardService.getPeakHoursStats(dataConsulta));
    }
}
