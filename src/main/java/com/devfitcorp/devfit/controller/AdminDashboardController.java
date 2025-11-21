package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.UsuarioDetalhadoDashboardDTO;
import com.devfitcorp.devfit.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/admin/dashboard") // Coreção do /api
public class AdminDashboardController {


    private final UsuarioService usuarioService;


    public AdminDashboardController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping("/usuarios/detalhado")
    public ResponseEntity<Map<String, List<UsuarioDetalhadoDashboardDTO>>> getUsersListDetalhado() {


        Map<String, List<UsuarioDetalhadoDashboardDTO>> usuariosAgrupados = usuarioService.findAndGroupByRole();
        Map<String, List<UsuarioDetalhadoDashboardDTO>> resultado = Optional.ofNullable(usuariosAgrupados)
                .orElse(Collections.emptyMap());

        return ResponseEntity.ok(usuariosAgrupados);
    }
}
