package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.FichaTreinoRequest;
import com.devfitcorp.devfit.dto.FichaTreinoResponse;
import com.devfitcorp.devfit.service.FichaTreinoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/criar")
    public ResponseEntity<FichaTreinoResponse> criar(@RequestBody @Valid FichaTreinoRequest request) {
        FichaTreinoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // LISTAR TODAS AS FICHAS
    @GetMapping
    public ResponseEntity<List<FichaTreinoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<FichaTreinoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // ATUALIZAR FICHA
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<FichaTreinoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FichaTreinoRequest request) {

        FichaTreinoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }


    // DELETAR FICHA
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
