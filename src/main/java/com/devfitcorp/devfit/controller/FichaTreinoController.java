package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.FichaTreinoRequest;
import com.devfitcorp.devfit.dto.FichaTreinoResponse;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.service.FichaTreinoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fichas/treino")
public class FichaTreinoController {

    private final FichaTreinoService service;

    public FichaTreinoController(FichaTreinoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GESTOR', 'INSTRUTOR')")
    public ResponseEntity<FichaTreinoResponse> criar(@RequestBody @Valid FichaTreinoRequest request) {
        FichaTreinoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('INSTRUTOR','GESTOR')")
    public ResponseEntity<List<FichaTreinoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUTOR','GESTOR')")
    public ResponseEntity<FichaTreinoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/minhas-fichas")
    @PreAuthorize("hasAnyRole('ALUNO', 'INSTRUTOR', 'GESTOR')")
    public ResponseEntity<List<FichaTreinoResponse>> listarMinhasFichas() {
        Usuario usuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long alunoId = usuarioAutenticado.getId();
        List<FichaTreinoResponse> fichas = service.buscarFichasPorId(alunoId);
        return ResponseEntity.ok(fichas);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GESTOR', 'INSTRUTOR')")
    public ResponseEntity<FichaTreinoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FichaTreinoRequest request) {

        FichaTreinoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
