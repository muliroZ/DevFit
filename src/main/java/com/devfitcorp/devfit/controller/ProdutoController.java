package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.devfitcorp.devfit.model.Produto;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final  ProdutoService produtoservice;

    public ProdutoController (ProdutoService produtoservice){
        this.produtoservice = produtoservice;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar(){
        List<Produto> produtos = produtoservice.listar();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id){
        Produto produto = produtoservice.buscarPorId(id);
        if (produto != null) {
            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid Produto produto){
        Produto salvo = produtoservice.salvar(produto);
        return ResponseEntity.status(201).body(salvo);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id,
                                             @RequestBody @Valid Produto produto) {

        Produto atualizado = produtoservice.atualizar(id, produto);

        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizado);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        Produto produto = produtoservice.buscarPorId(id);

        if (produto == null) {
            return ResponseEntity.notFound().build();
        }

        produtoservice.deletar(id);

        return ResponseEntity.noContent().build(); // 204
    }


}
