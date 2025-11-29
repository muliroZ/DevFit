package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.ProdutoRequest;
import com.devfitcorp.devfit.dto.ProdutoResponse;
import com.devfitcorp.devfit.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequest dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        produto.setImagemUrl(dto.imagemUrl());
        return produto;
    }

    public void updateEntityFromRequest(Produto produto, ProdutoRequest dto) {
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        produto.setImagemUrl(dto.imagemUrl());
    }

    public ProdutoResponse toResponse(Produto entity) {
        return new ProdutoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.getEstoque(),
                entity.getImagemUrl()
        );
    }
}
