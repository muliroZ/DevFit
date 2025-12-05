package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.ExercicioRequest;
import com.devfitcorp.devfit.model.Exercicio;
import com.devfitcorp.devfit.repository.ExercicioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExercicioService {

    private final ExercicioRepository repository;

    public ExercicioService(ExercicioRepository repository) {
        this.repository = repository;
    }

    public List<Exercicio> listar() {
        return repository.findAll();
    }

    public Exercicio salvar(ExercicioRequest request) {
        Exercicio ex = new Exercicio();
        ex.setNome(request.nome());
        ex.setMusculoPrincipal(request.musculoPrincipal());
        ex.setDescricao(request.descricao());
        return repository.save(ex);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}