package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.FichaTreinoRequest;
import com.devfitcorp.devfit.dto.FichaTreinoResponse;
import com.devfitcorp.devfit.exception.ResourceNotFoundException;
import com.devfitcorp.devfit.mappers.FichaTreinoMapper;
import com.devfitcorp.devfit.model.Exercicio;
import com.devfitcorp.devfit.model.FichaTreino;
import com.devfitcorp.devfit.model.ItemTreino;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.ExercicioRepository;
import com.devfitcorp.devfit.repository.FichaTreinoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class FichaTreinoService {

    private final FichaTreinoRepository fichaTreinoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExercicioRepository exercicioRepository;
    private final FichaTreinoMapper fichaTreinoMapper;

    public FichaTreinoService(FichaTreinoRepository fichaTreinoRepository,
                              UsuarioRepository usuarioRepository,
                              ExercicioRepository exercicioRepository,
                              FichaTreinoMapper fichaTreinoMapper) {
        this.fichaTreinoRepository = fichaTreinoRepository;
        this.usuarioRepository = usuarioRepository;
        this.exercicioRepository = exercicioRepository;
        this.fichaTreinoMapper = fichaTreinoMapper;
    }

    /**
     * ===================== CRIAR FICHA =====================
     * Cria uma nova ficha de treino a partir do DTO recebido.
     */
    public FichaTreinoResponse criar(FichaTreinoRequest dto) {

        // Busca o aluno e o instrutor pelo e-mail informado no DTO
        Usuario aluno = usuarioRepository.findByEmail(dto.emailAluno())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        Usuario instrutor = usuarioRepository.findByEmail(dto.emailInstrutor())
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado"));

        // Criação manual da entidade FichaTreino
        FichaTreino ficha = new FichaTreino();
        ficha.setAluno(aluno);
        ficha.setInstrutor(instrutor);
        ficha.setDataVencimento(dto.dataVencimento());
        ficha.setAtiva(true); // A ficha nasce ativa por padrão

        // Converte a lista de ItemTreinoRequest em lista de ItemTreino (entidade)
        List<ItemTreino> itens = dto.listaDeItens().stream().map(itemDto -> {

            // Busca o exercício base pelo ID informado
            Exercicio exercicio = exercicioRepository.findById(
                    itemDto.exercicioBase().getId()
            ).orElseThrow(() -> new ResourceNotFoundException("Exercício não encontrado"));

            // Cria um ItemTreino associado à ficha
            ItemTreino item = new ItemTreino();
            item.setFichaTreino(ficha);
            item.setExercicioBase(exercicio);
            item.setSeries(itemDto.series());
            item.setRepeticoes(itemDto.repeticoes());
            item.setCargaEstimadaKg(itemDto.cargaEstimadaKg());
            item.setObservacoes(itemDto.observacoes());

            return item;
        }).toList();

        // Associa os itens criados à ficha
        ficha.setListaDeItens(itens);

        // Salva a ficha e os itens no banco
        fichaTreinoRepository.save(ficha);

        // Retorna a resposta usando o mapper
        return fichaTreinoMapper.toResponse(ficha);
    }

    /**
     * ===================== LISTAR TODAS =====================
     * Retorna todas as fichas de treino existentes.
     */
    public List<FichaTreinoResponse> listar() {
        return fichaTreinoRepository.findAll()
                .stream()
                .map(fichaTreinoMapper::toResponse)
                .toList();
    }

    /**
     * ===================== BUSCAR POR ID =====================
     * Retorna uma ficha específica pelo ID.
     */
    public FichaTreinoResponse buscarPorId(Long id) {
        FichaTreino ficha = fichaTreinoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ficha de treino não encontrada: " + id));

        return fichaTreinoMapper.toResponse(ficha);
    }

    /**
     * ===================== ATUALIZAR FICHA =====================
     * Atualiza uma ficha existente com base no ID.
     */
    public FichaTreinoResponse atualizar(Long id, FichaTreinoRequest dto) {

        // Busca a ficha existente
        FichaTreino ficha = fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha não encontrada: " + id));

        // Atualiza os usuários associados
        Usuario aluno = usuarioRepository.findByEmail(dto.emailAluno())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        Usuario instrutor = usuarioRepository.findByEmail(dto.emailInstrutor())
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado"));

        ficha.setAluno(aluno);
        ficha.setInstrutor(instrutor);
        ficha.setDataVencimento(dto.dataVencimento());

        // Remove os itens antigos da ficha
        ficha.getListaDeItens().clear();

        // Recria a lista de itens com base no DTO
        List<ItemTreino> novosItens = dto.listaDeItens().stream().map(itemDto -> {

            Exercicio exercicio = exercicioRepository.findById(
                    itemDto.exercicioBase().getId()
            ).orElseThrow(() -> new ResourceNotFoundException("Exercício não encontrado"));

            ItemTreino item = new ItemTreino();
            item.setFichaTreino(ficha);
            item.setExercicioBase(exercicio);
            item.setSeries(itemDto.series());
            item.setRepeticoes(itemDto.repeticoes());
            item.setCargaEstimadaKg(itemDto.cargaEstimadaKg());
            item.setObservacoes(itemDto.observacoes());

            return item;
        }).toList();

        ficha.getListaDeItens().addAll(novosItens);

        // Persiste alterações no banco
        fichaTreinoRepository.save(ficha);

        return fichaTreinoMapper.toResponse(ficha);
    }

    /**
     * ===================== DELETAR FICHA =====================
     * Remove uma ficha pelo ID.
     */
    public void deletar(Long id) {

        FichaTreino ficha = fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha não encontrada: " + id));

        fichaTreinoRepository.delete(ficha);
    }
}