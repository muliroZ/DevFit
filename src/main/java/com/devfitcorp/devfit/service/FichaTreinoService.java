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
//Busca ficha de treino por ID
    public FichaTreino buscarPorId(Long id) {
        return fichaTreinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ficha de treino não encontrada: " + id));
    }
//Cria uma nova ficha de treino associando-a a um aluno e um instrutor
    public FichaTreino criar(Long alunoId, Long instrutorId, FichaTreino fichaTreino) {

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado: " + alunoId));

        Usuario instrutor = usuarioRepository.findById(instrutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado: " + instrutorId));

        fichaTreino.setAluno(aluno);
        fichaTreino.setInstrutor(instrutor);

        return fichaTreinoRepository.save(fichaTreino);
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
}
