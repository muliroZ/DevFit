package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.model.Produto;
import com.devfitcorp.devfit.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/public/produtos")
public class PublicProdutoController {

    private final ProdutoService produtoService;

    public PublicProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid Produto produto) {
        Produto salvo = produtoService.salvar(produto);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid Produto produto
    ) {
        Produto atualizado = produtoService.atualizar(id, produto);
        if (atualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) return ResponseEntity.notFound().build();

        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
