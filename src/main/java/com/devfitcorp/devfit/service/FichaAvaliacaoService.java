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

    public FichaAvaliacaoService(
            FichaAvaliacaoRepository fichaAvaliacaoRepository,
            UsuarioRepository usuarioRepository,
            FichaAvaliacaoMapper fichaAvaliacaoMapper
    ) {
        this.fichaAvaliacaoRepository = fichaAvaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.fichaAvaliacaoMapper = fichaAvaliacaoMapper;
    }

    @Transactional
    public FichaAvaliacaoResponse criar(FichaAvaliacaoRequest dto) {
        Usuario aluno = usuarioRepository.findByIdAndRoles_Nome(dto.idAluno(), UsuarioRole.ALUNO)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        Usuario instrutor = usuarioRepository.findByIdAndRoles_Nome(dto.idInstrutor(), UsuarioRole.INSTRUTOR)
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado"));

        FichaAvaliacao ficha = fichaAvaliacaoMapper.toEntity(dto, aluno, instrutor, LocalDate.now());
        double imcCalculado = calcularImc(ficha.getPesoKg(), ficha.getAlturaCm());
        ficha.setImc(imcCalculado);

        FichaAvaliacao savedFicha = fichaAvaliacaoRepository.save(ficha);

        return fichaAvaliacaoMapper.toResponse(savedFicha);
    }

    public List<FichaAvaliacaoResponse> buscarFichasPorId(Long alunoId) {
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

        FichaAvaliacao savedFicha = fichaAvaliacaoRepository.save(fichaAtualizada);
        return fichaAvaliacaoMapper.toResponse(savedFicha);
    }

    @Transactional
    public void deletar(Long id) {
        if (!fichaAvaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ficha não encontrada: " + id);
        }
        fichaAvaliacaoRepository.deleteById(id);
    }

    // método para calcular o IMC
    private double calcularImc(Double pesoKg, Double alturaCm) {
        if (pesoKg == null || pesoKg <= 0 || alturaCm == null || alturaCm == 0) {
            throw new IllegalArgumentException("Altura/Peso deve ser maior que zero.");
        }
        return pesoKg / ((alturaCm / 100) * (alturaCm / 100));
    }

    private Usuario buscarUsuarioPorIdERole(Long id, UsuarioRole role) {
        return usuarioRepository.findByIdAndRoles_Nome(id, role)
                .orElseThrow(() -> new ResourceNotFoundException(role.name().concat(" não encontrado")));
    }

}
