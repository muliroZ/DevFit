package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.ExercicioRequest;
import com.devfitcorp.devfit.model.Exercicio;
import com.devfitcorp.devfit.service.ExercicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercicios")
public class ExercicioController {

    private final ExercicioService exercicioService;

    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }

    @GetMapping
    public List<Exercicio> listarTodos() {
        return exercicioService.listar();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'GESTOR')")
    public ResponseEntity<Exercicio> criar(@RequestBody @Valid ExercicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exercicioService.salvar(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUTOR', 'GESTOR')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        exercicioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}