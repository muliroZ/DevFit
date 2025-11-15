package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.UsuarioDetalhadoDashboardDTO; // DTO que criamos
import com.devfitcorp.devfit.service.UsuarioService; // Service de lógica de negócio
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Spring Security para autorização
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {


    private final UsuarioService usuarioService;


    public AdminDashboardController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/usuarios/detalhado")
    public ResponseEntity<Map<String, List<UsuarioDetalhadoDashboardDTO>>> getUsersListDetalhado() {


        Map<String, List<UsuarioDetalhadoDashboardDTO>> usuariosAgrupados = usuarioService.findAndGroupByRole();

        return ResponseEntity.ok(usuariosAgrupados);
    }

}