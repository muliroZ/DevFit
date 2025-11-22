package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.exception.ResourceNotFoundException;
import com.devfitcorp.devfit.model.FichaTreino;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.FichaTreinoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaTreinoService {

    private final FichaTreinoRepository fichaTreinoRepository;
    private final UsuarioRepository usuarioRepository;
    //Injeção de dependência via construtor
    public FichaTreinoService(FichaTreinoRepository fichaTreinoRepository,
                              UsuarioRepository usuarioRepository) {
        this.fichaTreinoRepository = fichaTreinoRepository;
        this.usuarioRepository = usuarioRepository;
    }
//Lista de todas as fichas de treino
    public List<FichaTreino> listar() {
        return fichaTreinoRepository.findAll();
    }

    //========= Operações por ID==========

//Busca ficha de treino por ID
    public FichaTreino buscarPorId(Long id) {
        return fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha de treino não encontrada: " + id));
    }

//atualiza os dados de uma ficha de treino existente
    public FichaTreino atualizar(Long id, FichaTreino dadosAtualizados) {
        FichaTreino ficha = buscarPorId(id);

        ficha.setDataVencimento(dadosAtualizados.getDataVencimento());
        ficha.setAtiva(dadosAtualizados.isAtiva());

        return fichaTreinoRepository.save(ficha);
    }

//Deleta uma ficha de treino por ID
    public void deletar(Long id) {
        buscarPorId(id);
        fichaTreinoRepository.deleteById(id);
    }

 //Lista todas as fichas de treino associadas a um aluno específico
    public List<FichaTreino> listarPorAluno(Long alunoId) {
        return fichaTreinoRepository.FindByAlunoId(alunoId);
    }



//=================Operações por e-mail ==================

    //Cria de uma nova ficha de treino associando-a ao e-mail um aluno e um instrutor
public FichaTreino criar(String emailAluno, String emailInstrutor, FichaTreino fichaTreino) {

    Usuario aluno = usuarioRepository.findByEmail(emailAluno)
            .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + emailAluno));

    Usuario instrutor = usuarioRepository.findByEmail(emailInstrutor)
            .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado: " + emailInstrutor));

    fichaTreino.setAluno(aluno);
    fichaTreino.setInstrutor(instrutor);

    return fichaTreinoRepository.save(fichaTreino);
}

    // Lista todas as fichas de treino associadas ao e-mail de um aluno específico

    public List<FichaTreino> listarPorEmailAluno(String emailAluno) {

        Usuario aluno = usuarioRepository.findByEmail(emailAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + emailAluno));

        return fichaTreinoRepository.FindByAlunoId(aluno.getId());
    }
  // Busca a primeira ficha de treino do aluno pelo e-mail
    public FichaTreino buscarPorEmailAluno(String emailAluno) {

        Usuario aluno = usuarioRepository.findByEmail(emailAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + emailAluno));

        return fichaTreinoRepository.FindByAlunoId(aluno.getId())
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ficha de treino não encontrada para o aluno"));
    }

// Atualiza os dados de uma ficha de treino existente por e-mail do aluno
    public FichaTreino atualizarPorEmail(String emailAluno, FichaTreino dadosAtualizados) {

        FichaTreino ficha = buscarPorEmailAluno(emailAluno);

        ficha.setDataVencimento(dadosAtualizados.getDataVencimento());
        ficha.setAtiva(dadosAtualizados.isAtiva());

        return fichaTreinoRepository.save(ficha);
    }
// Remove todas as fichas de treino associadas ao e-mail de um aluno específico
    public void deletarPorEmail(String emailAluno) {

        Usuario aluno = usuarioRepository.findByEmail(emailAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + emailAluno));

        List<FichaTreino> fichas = fichaTreinoRepository.FindByAlunoId(aluno.getId());

        if (fichas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma ficha encontrada para o aluno");
        }

        fichaTreinoRepository.deleteAll(fichas);
    }
}