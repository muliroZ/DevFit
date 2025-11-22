package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.FichaAvaliacaoRequest;
import com.devfitcorp.devfit.dto.FichaAvaliacaoResponse;
import com.devfitcorp.devfit.exception.ResourceNotFoundException;
import com.devfitcorp.devfit.mappers.FichaAvaliacaoMapper;
import com.devfitcorp.devfit.model.FichaAvaliacao;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.FichaAvaliacaoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FichaAvaliacaoService {

    private final FichaAvaliacaoRepository fichaAvaliacaoRepository;
    private final UsuarioRepository usuarioRepository; // Para findByEmail()
    private final FichaAvaliacaoMapper fichaAvaliacaoMapper;

    @Autowired
    public FichaAvaliacaoService(
            FichaAvaliacaoRepository fichaAvaliacaoRepository,
            UsuarioRepository usuarioRepository,
            FichaAvaliacaoMapper fichaAvaliacaoMapper) {
        this.fichaAvaliacaoRepository = fichaAvaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.fichaAvaliacaoMapper = fichaAvaliacaoMapper;
    }

    private double calcularImc(Double pesoKg, Double alturaM) {
        if (pesoKg ==null || pesoKg <= 0 || alturaM == null || alturaM == 0) {
            throw new IllegalArgumentException("Altura deve ser maior que zero.");
        }
        return pesoKg / (alturaM * alturaM);
    }

    private Usuario findUsuarioByEmail(String email, String role) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(role + "não encontrado com o email: " + email));

    }
     // Create FichaAvaliacao, recebe DTO
    @Transactional
    public FichaAvaliacaoResponse create(FichaAvaliacaoRequest dto) {
             //Busca os usuarios pelo email
        Usuario aluno = findUsuarioByEmail(dto.emailAluno(), "Aluno ");
        Usuario instrutor = findUsuarioByEmail(dto.emailInstrutor(), "Instrutor ");

        FichaAvaliacao ficha = fichaAvaliacaoMapper.toEntity(dto, aluno, instrutor);
        double imcCalculado = calcularImc(ficha.getPesoKg(), ficha.getAlturaM());
        ficha.setImc(imcCalculado);
            // salvamento da ficha
        FichaAvaliacao savedFicha = fichaAvaliacaoRepository.save(ficha);

        return fichaAvaliacaoMapper.toResponse(savedFicha);
    }

    //Read. busca uma ficha pelo id e converte para response DTO
    public FichaAvaliacaoResponse findbyId(Long id) {
        FichaAvaliacao ficha = fichaAvaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return fichaAvaliacaoMapper.toResponse(ficha);
    }

    public List<FichaAvaliacaoResponse> findByAlunoId(Long alunoId) {
        return fichaAvaliacaoRepository.FindByAlunoId(alunoId).stream()
                .map(fichaAvaliacaoMapper::toResponse)
                .collect(Collectors.toList());
    }
    public List<FichaAvaliacaoResponse> findAll() {
        return fichaAvaliacaoRepository.findAll().stream()
                .map(fichaAvaliacaoMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public void delete(Long id) {
        if (!fichaAvaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        fichaAvaliacaoRepository.deleteById(id);
    }

}
