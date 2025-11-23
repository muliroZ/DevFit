package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.FichaTreinoRequest;
import com.devfitcorp.devfit.dto.FichaTreinoResponse;
import com.devfitcorp.devfit.exception.ResourceNotFoundException;
import com.devfitcorp.devfit.mappers.FichaTreinoMapper;
import com.devfitcorp.devfit.model.*;
import com.devfitcorp.devfit.repository.ExercicioRepository;
import com.devfitcorp.devfit.repository.FichaTreinoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FichaTreinoService {

    private final FichaTreinoRepository fichaTreinoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExercicioRepository exercicioRepository;
    private final FichaTreinoMapper fichaTreinoMapper;

    public FichaTreinoService(
            FichaTreinoRepository fichaTreinoRepository,
            UsuarioRepository usuarioRepository,
            ExercicioRepository exercicioRepository,
            FichaTreinoMapper fichaTreinoMapper
    ) {
        this.fichaTreinoRepository = fichaTreinoRepository;
        this.usuarioRepository = usuarioRepository;
        this.exercicioRepository = exercicioRepository;
        this.fichaTreinoMapper = fichaTreinoMapper;
    }

    // Cria uma ficha de treino a partir do DTO
    @Transactional // Garantir integridade no banco (use em todos os métodos de escrita garoto)
    public FichaTreinoResponse criar(FichaTreinoRequest dto) {
        // Usando a função auxiliar "escrever"
        return escrever(dto);
    }

    // Retorna todas as fichas de treino existentes
    public List<FichaTreinoResponse> listar() {
        return fichaTreinoRepository.findAll()
                .stream()
                .map(fichaTreinoMapper::toResponse)
                .toList();
    }

    // Busca uma ficha específica pelo ID
    public FichaTreinoResponse buscarPorId(Long id) {
        FichaTreino ficha = fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha de treino não encontrada: " + id));

        return fichaTreinoMapper.toResponse(ficha);
    }

    // Atualiza uma ficha existente com base no DTO
    @Transactional
    public FichaTreinoResponse atualizar(Long id, FichaTreinoRequest dto) {
        // Verifica se a ficha existe no banco
        if (!fichaTreinoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ficha não encontrada");
        }
        return escrever(dto);
    }

    // Remove uma ficha pelo ID
    @Transactional
    public void deletar(Long id) {
        FichaTreino ficha = fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha não encontrada: " + id));

        fichaTreinoRepository.delete(ficha);
    }

    // método auxiliar para métodos de criação e atualização de fichas (esse código se repetia)
    @Transactional
    protected FichaTreinoResponse escrever(FichaTreinoRequest request) {
        Usuario aluno = usuarioRepository.findById(request.idAluno())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        Usuario instrutor = usuarioRepository.findById(request.idInstrutor())
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado"));

        List<ItemTreino> itens = listarItensDoRequest(request);
        FichaTreino ficha = fichaTreinoMapper.toEntity(request, aluno, instrutor, itens);

        fichaTreinoRepository.save(ficha);
        return fichaTreinoMapper.toResponse(ficha);
    }

    // método auxiliar para listar ItemTreino dos DTOs de FichaTreino usando o Mapper (estava se repetindo)
    private List<ItemTreino> listarItensDoRequest(FichaTreinoRequest request) {
        return request.listaDeItens().stream()
                .map(item -> fichaTreinoMapper
                        .toEntity(item, exercicioRepository.findExercicioById(item.exercicioId())))
                .toList();
    }
}