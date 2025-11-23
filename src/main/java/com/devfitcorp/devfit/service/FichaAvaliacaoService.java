package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.FichaAvaliacaoRequest;
import com.devfitcorp.devfit.dto.FichaAvaliacaoResponse;
import com.devfitcorp.devfit.exception.ResourceNotFoundException;
import com.devfitcorp.devfit.mappers.FichaAvaliacaoMapper;
import com.devfitcorp.devfit.model.FichaAvaliacao;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.model.UsuarioRole;
import com.devfitcorp.devfit.repository.FichaAvaliacaoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FichaAvaliacaoService {

    private final FichaAvaliacaoRepository fichaAvaliacaoRepository;
    private final UsuarioRepository usuarioRepository; // Para findByEmail()
    private final FichaAvaliacaoMapper fichaAvaliacaoMapper;

    // Injeção de dependências via construtor
    public FichaAvaliacaoService(
            FichaAvaliacaoRepository fichaAvaliacaoRepository,
            UsuarioRepository usuarioRepository,
            FichaAvaliacaoMapper fichaAvaliacaoMapper
    ) {
        this.fichaAvaliacaoRepository = fichaAvaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.fichaAvaliacaoMapper = fichaAvaliacaoMapper;
    }

    // CRUD: CREATE, FichaAvaliacao, recebe DTO
    @Transactional
    public FichaAvaliacaoResponse criar(FichaAvaliacaoRequest dto) {
        // Busca os usuarios pelo id e role
        Usuario aluno = usuarioRepository.findByIdAndRole(dto.idAluno(), UsuarioRole.ALUNO)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        Usuario instrutor = usuarioRepository.findByIdAndRole(dto.idInstrutor(), UsuarioRole.INSTRUTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado"));

        FichaAvaliacao ficha = fichaAvaliacaoMapper.toEntity(dto, aluno, instrutor, LocalDate.now());
        double imcCalculado = calcularImc(ficha.getPesoKg(), ficha.getAlturaCm());
        ficha.setImc(imcCalculado);

        FichaAvaliacao savedFicha = fichaAvaliacaoRepository.save(ficha);

        return fichaAvaliacaoMapper.toResponse(savedFicha);
    }

    // CRUD: READ, ficha com id específico
    public List<FichaAvaliacaoResponse> buscarPorId(Long alunoId) {
        return fichaAvaliacaoRepository.findByAlunoId(alunoId).stream()
                .map(fichaAvaliacaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    // CRUD: READ, Todas as fichas existentes
    public List<FichaAvaliacaoResponse> listar() {
        return fichaAvaliacaoRepository.findAll().stream()
                .map(fichaAvaliacaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    //CRUD: UPDATE, FichaAvaliacao
    @Transactional
    public FichaAvaliacaoResponse atualizar(Long id, FichaAvaliacaoRequest dto) {
        FichaAvaliacao fichaAntiga = fichaAvaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha não encontrada"));

        Usuario instrutor = buscarUsuarioPorIdERole(dto.idInstrutor(), UsuarioRole.INSTRUTOR);
        Usuario aluno = buscarUsuarioPorIdERole(dto.idAluno(), UsuarioRole.ALUNO);

        FichaAvaliacao fichaAtualizada = fichaAvaliacaoMapper.toEntity(dto, aluno, instrutor, LocalDate.now());

        fichaAtualizada.setId(fichaAntiga.getId());

        double imcCalculado = calcularImc(fichaAtualizada.getPesoKg(), fichaAtualizada.getAlturaCm());
        fichaAtualizada.setImc(imcCalculado);

        /* Variável 'savedFicha' é igual a 'fichaAtualizada', use somente a 'fichaAtualizada'
            FichaAvaliacao savedFicha = fichaAvaliacaoRepository.save(fichaAtualizada);
            return fichaAvaliacaoMapper.toResponse(savedFicha);
         */

        fichaAvaliacaoRepository.save(fichaAtualizada);
        return fichaAvaliacaoMapper.toResponse(fichaAtualizada);
    }

    // CRUD: DELETE, exclui uma ficha conforme o id
    @Transactional
    public void deletar(Long id) {
        if (!fichaAvaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ficha não encontrada: " + id);
        }
        fichaAvaliacaoRepository.deleteById(id);
    }

    // método para calcular o IMC
    private double calcularImc(Double pesoKg, Double alturaM) {
        if (pesoKg == null || pesoKg <= 0 || alturaM == null || alturaM == 0) {
            throw new IllegalArgumentException("Altura deve ser maior que zero.");
        }
        return pesoKg / (alturaM * alturaM);
    }

    // método auxiliar para buscar os alunos e instrutores por id e role (estava se repetindo)
    private Usuario buscarUsuarioPorIdERole(Long id, UsuarioRole role) {
        return usuarioRepository.findByIdAndRole(id, role)
                .orElseThrow(() -> new ResourceNotFoundException(role.name().concat(" não encontrado")));
    }
}
