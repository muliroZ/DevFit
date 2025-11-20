package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.devfitcorp.devfit.model.Produto;
import java.util.List;
import com.devfitcorp.devfit.dto.ProdutoResponse;
import com.devfitcorp.devfit.dto.ProdutoRequest;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final  ProdutoService produtoservice;

    public ProdutoController (ProdutoService produtoservice){
        this.produtoservice = produtoservice;
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return produtoservice.listar()
                .stream()
                .map(p -> new ProdutoResponse(
                        p.getId(),
                        p.getNome(),
                        p.getDescricao(),
                        p.getPreco(),
                        p.getEstoque()
                ))
                .toList();
    }


    @GetMapping("/buscar/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id){
        var produto = produtoservice.buscarPorId(id);

        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque()
        );
    }

    @PostMapping("/adicionar")
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest request){
        var produto = new Produto(
                null,
                request.nome(),
                request.descricao(),
                request.preco(),
                request.estoque()
        );

        var salvo = produtoservice.salvar(produto);

        var response = new ProdutoResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getDescricao(),
                salvo.getPreco(),
                salvo.getEstoque()
        );

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/atualizar/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest request) {
        var dadosAtualizados = new Produto(
                null,
                request.nome(),
                request.descricao(),
                request.preco(),
                request.estoque()
        );
        var atualizado = produtoservice.atualizar(id, dadosAtualizados);

        return new ProdutoResponse(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getDescricao(),
                atualizado.getPreco(),
                atualizado.getEstoque()
        );

    }
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        produtoservice.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
