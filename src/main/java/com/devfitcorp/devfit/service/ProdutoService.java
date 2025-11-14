package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import com.devfitcorp.devfit.model.Produto;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }
    public List <Produto> listar(){
        return repository.findAll();
    }
    public Produto buscarPorId(Long id){
        return repository.findById(id)
                .orElse(null);
    }
    public Produto salvar(Produto produto){
        return repository.save(produto);
    }
    public void deletar(Long id){
        repository.deleteById(id);
    }
    public Produto atualizar(Long id, Produto dadosAtualizados) {
        Produto existente = repository.findById(id).orElse(null);

        if (existente == null) {
            return null;
        }

        existente.setNome(dadosAtualizados.getNome());
        existente.setDescricao(dadosAtualizados.getDescricao());
        existente.setPreco(dadosAtualizados.getPreco());
        existente.setEstoque(dadosAtualizados.getEstoque());

        return repository.save(existente);
    }


}
