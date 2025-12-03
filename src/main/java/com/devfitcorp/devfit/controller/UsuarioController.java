package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.UsuarioInfoDTO;
import com.devfitcorp.devfit.model.UsuarioRole;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/alunos")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'GESTOR')")
    public List<UsuarioInfoDTO> listarAlunos() {
        return repository.findByRoles_Nome(UsuarioRole.ALUNO).stream()
                .map(u -> new UsuarioInfoDTO(u.getId(), u.getNome(), u.getEmail()))
                .toList();
    }
}