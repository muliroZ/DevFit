package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.ProdutoRequest;
import com.devfitcorp.devfit.dto.ProdutoResponse;
import com.devfitcorp.devfit.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return produtoService.listar();
    }

    @GetMapping("/buscar/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id){
        return produtoService.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    @PostMapping("/adicionar")
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest request){
        ProdutoResponse response = produtoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    @PutMapping("/atualizar/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest request) {
        return produtoService.atualizar(id, request);
    }

    @PreAuthorize("hasAuthority('ROLE_GESTOR')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
