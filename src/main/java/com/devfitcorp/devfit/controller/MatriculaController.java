package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.model.Matricula;
import com.devfitcorp.devfit.service.MatriculaService;
import com.devfitcorp.devfit.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matricula")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final UsuarioService usuarioService;

    public MatriculaController(MatriculaService matriculaService, UsuarioService usuarioService) {
        this.matriculaService = matriculaService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/assinar/{planoId}")
    public ResponseEntity<Matricula> assinarPlano(
            @PathVariable Long planoId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Long usuarioId = usuarioService.getUserIdFromUsername(userDetails.getUsername());

        Matricula novaMatricula = matriculaService.assinarPlano(usuarioId, planoId);
        return ResponseEntity.status(201).body(novaMatricula);
    }
}