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
import java.util.stream.Collectors;

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
        Usuario aluno = buscarUsuarioPorIdERole(dto.idAluno(), UsuarioRole.ALUNO);
        Usuario instrutor = buscarUsuarioPorIdERole(dto.idInstrutor(), UsuarioRole.INSTRUTOR);

        List<ItemTreino> itens = listarItensDoRequest(dto);
        FichaTreino ficha = fichaTreinoMapper.toEntity(dto, aluno, instrutor, itens);

        fichaTreinoRepository.save(ficha);
        return fichaTreinoMapper.toResponse(ficha);
    }

    // Retorna todas as fichas de treino existentes
    public List<FichaTreinoResponse> listar() {
        return fichaTreinoRepository.findAll()
                .stream()
                .map(fichaTreinoMapper::toResponse)
                .toList();
    }
    public List<FichaTreinoResponse> buscarFichasPorId(Long alunoId) {
        return fichaTreinoRepository.findByAlunoId(alunoId).stream()
                .map(fichaTreinoMapper::toResponse)
                .collect(Collectors.toList());
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
        FichaTreino existente = fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha não encontrada"));

        Usuario aluno = buscarUsuarioPorIdERole(dto.idAluno(), UsuarioRole.ALUNO);
        Usuario instrutor = buscarUsuarioPorIdERole(dto.idInstrutor(), UsuarioRole.INSTRUTOR);

        List<ItemTreino> itens = listarItensDoRequest(dto);
        FichaTreino atualizada = fichaTreinoMapper.toEntity(dto, aluno, instrutor, itens);

        atualizada.setId(existente.getId());

        fichaTreinoRepository.save(atualizada);
        return fichaTreinoMapper.toResponse(atualizada);
    }

    // Remove uma ficha pelo ID
    @Transactional
    public void deletar(Long id) {
        FichaTreino ficha = fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha não encontrada: " + id));

        fichaTreinoRepository.delete(ficha);
    }

    // método auxiliar para buscar os alunos e instrutores por id e role (estava se repetindo)
    private Usuario buscarUsuarioPorIdERole(Long id, UsuarioRole role) {
        return usuarioRepository.findByIdAndRoles_Nome(id, role)
                .orElseThrow(() -> new ResourceNotFoundException(role.name().concat(" não encontrado")));
    }

    // método auxiliar para listar ItemTreino dos DTOs de FichaTreino usando o Mapper (estava se repetindo)
    private List<ItemTreino> listarItensDoRequest(FichaTreinoRequest request) {
        return request.listaDeItens().stream()
                .map(item -> fichaTreinoMapper
                        .toEntity(item, exercicioRepository.findExercicioById(item.exercicioId())))
                .toList();
    }
}