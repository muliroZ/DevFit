package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.model.Matricula;
import com.devfitcorp.devfit.model.Plano;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.MatriculaRepository;
import com.devfitcorp.devfit.repository.PlanoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final PlanoRepository planoRepository;
    private final UsuarioRepository usuarioRepository;

    // Construtor para Injeção de Dependências
    public MatriculaService(
            MatriculaRepository matriculaRepository,
            PlanoRepository planoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.matriculaRepository = matriculaRepository;
        this.planoRepository = planoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Matricula assinarPlano(Long usuarioId, Long planoId) {

        Usuario aluno = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        Plano plano = planoRepository.findById(planoId)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado."));

        if (!plano.isAtivo()) {
            throw new IllegalArgumentException("O plano selecionado não está ativo.");
        }

        matriculaRepository.findTopByUsuarioIdAndIsAtivaTrueOrderByDataVencimentoDesc(usuarioId)
                .ifPresent(mat -> {
                    mat.setAtiva(false);
                    matriculaRepository.save(mat);
                });

        Matricula novaMatricula = new Matricula();
        LocalDate hoje = LocalDate.now();

        novaMatricula.setUsuario(aluno);
        novaMatricula.setPlano(plano);
        novaMatricula.setDataInicio(hoje);
        novaMatricula.setDataVencimento(hoje.plusMonths(plano.getDuracaoMeses()));

        novaMatricula.setAtiva(true);
        return matriculaRepository.save(novaMatricula);
    }

    public boolean isMatriculaAtiva(Long usuarioId) {
        Optional<Matricula> matriculaOpt = matriculaRepository
                .findTopByUsuarioIdAndIsAtivaTrueOrderByDataVencimentoDesc(usuarioId);
        if (matriculaOpt.isEmpty()) {
            return false;
        }

        Matricula matricula = matriculaOpt.get();

        if (!matricula.isAtiva()) {
            return false;
        }

        return matricula.getDataVencimento().isAfter(LocalDate.now().minusDays(1));
    }
}