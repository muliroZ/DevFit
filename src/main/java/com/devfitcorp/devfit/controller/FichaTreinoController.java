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
//injeção de dependência via construtor
    public FichaTreinoController(FichaTreinoService service) {
        this.service = service;
    }

    // CRIAR FICHA DE TREINO
    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    public ResponseEntity<FichaTreinoResponse> criar(@RequestBody @Valid FichaTreinoRequest request) {
        FichaTreinoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // LISTAR TODAS AS FICHAS
    @GetMapping
    @PreAuthorize("hasAnyRole('Instrutor','Gerente')")
    public ResponseEntity<List<FichaTreinoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Instrutor','Gerente')")
    public ResponseEntity<FichaTreinoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // falta por o metodo para o aluno ver suas proprias fichas de treino
    // tentei colocar e constou um erro, vou deixar comentado por enquanto

    // ATUALIZAR FICHA
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'INSTRUTOR')")
    public ResponseEntity<FichaTreinoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FichaTreinoRequest request) {

        FichaTreinoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }


    // DELETAR FICHA
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
