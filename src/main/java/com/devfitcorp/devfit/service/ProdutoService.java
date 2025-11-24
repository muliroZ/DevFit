package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.ProdutoRequest;
import com.devfitcorp.devfit.dto.ProdutoResponse;
import com.devfitcorp.devfit.exception.ProdutoNaoEncontradoException;
import com.devfitcorp.devfit.mappers.ProdutoMapper;
import com.devfitcorp.devfit.model.Produto;
import com.devfitcorp.devfit.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    public ProdutoService(ProdutoRepository repository, ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ProdutoResponse> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        return mapper.toResponse(produto);
    }

    public ProdutoResponse salvar(ProdutoRequest request) {
        Produto produto = mapper.toEntity(request);
        Produto salvo = repository.save(produto);
        return mapper.toResponse(salvo);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto existente = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        mapper.updateEntityFromRequest(existente, request);
        Produto atualizado = repository.save(existente);

        return mapper.toResponse(atualizado);
    }

    public void deletar(Long id) {
        Produto existente = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        repository.delete(existente);
    }
}
