package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.exception.ResourceNotFoundException;
import com.devfitcorp.devfit.model.FichaAvaliacao;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.FichaAvaliacaoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaAvaliacaoService {

    private final FichaAvaliacaoRepository fichaAvaliacaoRepository;
    private final UsuarioRepository usuarioRepository;

    // Injeção de dependência via construtor
    public FichaAvaliacaoService(FichaAvaliacaoRepository fichaAvaliacaoRepository,
                                 UsuarioRepository usuarioRepository) {
        this.fichaAvaliacaoRepository = fichaAvaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Lista todas as fichas de avaliação
    public List<FichaAvaliacao> listar() {
        return fichaAvaliacaoRepository.findAll();
    }

    // Busca ficha de avaliação por ID
    public FichaAvaliacao buscarPorId(Long id) {
        return fichaAvaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha de avaliação não encontrada: " + id));
    }

    // Cria uma nova ficha de avaliação associando-a a um aluno e um instrutor
    public FichaAvaliacao criar(Long alunoId, Long instrutorId, FichaAvaliacao fichaAvaliacao) {

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + alunoId));

        Usuario instrutor = usuarioRepository.findById(instrutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado: " + instrutorId));

        fichaAvaliacao.setAluno(aluno);
        fichaAvaliacao.setInstrutor(instrutor);

        // Calcula automaticamente o IMC
        double imc = fichaAvaliacao.getPesoKg() /
                (fichaAvaliacao.getAlturaM() * fichaAvaliacao.getAlturaM());
        fichaAvaliacao.setImc(imc);

        return fichaAvaliacaoRepository.save(fichaAvaliacao);
    }

    // Atualiza os dados de uma ficha de avaliação existente
    public FichaAvaliacao atualizar(Long id, FichaAvaliacao dadosAtualizados) {

        FichaAvaliacao ficha = buscarPorId(id);

        ficha.setPesoKg(dadosAtualizados.getPesoKg());
        ficha.setAlturaM(dadosAtualizados.getAlturaM());
        ficha.setCircunferenciaCinturaCm(dadosAtualizados.getCircunferenciaCinturaCm());
        ficha.setCircunferenciaAbdomenCm(dadosAtualizados.getCircunferenciaAbdomenCm());
        ficha.setCircunferenciaQuadrilCm(dadosAtualizados.getCircunferenciaQuadrilCm());
        ficha.setHistoricoSaude(dadosAtualizados.getHistoricoSaude());
        ficha.setObservacoesGerais(dadosAtualizados.getObservacoesGerais());

        // Recalcula o IMC após atualização
        double imc = ficha.getPesoKg() / (ficha.getAlturaM() * ficha.getAlturaM());
        ficha.setImc(imc);

        return fichaAvaliacaoRepository.save(ficha);
    }

    // Remove uma ficha de avaliação pelo ID
    public void deletar(Long id) {
        buscarPorId(id);
        fichaAvaliacaoRepository.deleteById(id);
    }

    // Lista todas as fichas de avaliação associadas a um aluno específico
    public List<FichaAvaliacao> listarPorAluno(Long alunoId) {
        return fichaAvaliacaoRepository.FindByAlunoId(alunoId);
    }

}
