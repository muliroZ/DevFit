package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.exception.ProdutoNaoEncontradoException;
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
                .orElseThrow(()-> new ProdutoNaoEncontradoException(id));
    }
    public Produto salvar(Produto produto){
        return repository.save(produto);
    }
    public void deletar(Long id){
        buscarPorId(id);
        repository.deleteById(id);
    }
    public Produto atualizar(Long id, Produto dadosAtualizados) {
        Produto existente = buscarPorId(id);


        existente.setNome(dadosAtualizados.getNome());
        existente.setDescricao(dadosAtualizados.getDescricao());
        existente.setPreco(dadosAtualizados.getPreco());
        existente.setEstoque(dadosAtualizados.getEstoque());

        return repository.save(existente);
    }


}
