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
    public List<Produto> listar(){
       return produtoservice.listar();

    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id){
        return produtoservice.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody @Valid Produto produto){
        Produto salvo = produtoservice.salvar(produto);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody @Valid Produto produto) {
        return produtoservice.atualizar(id, produto);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        produtoservice.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
