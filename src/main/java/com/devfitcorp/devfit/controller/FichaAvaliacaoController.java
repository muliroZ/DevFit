package com.devfitcorp.devfit.controller;


import com.devfitcorp.devfit.dto.FichaAvaliacaoRequest;
import com.devfitcorp.devfit.dto.FichaAvaliacaoResponse;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.service.FichaAvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fichas/avaliacao") // estou com uma duvida sobre os endpoints. confere murilo?
public class FichaAvaliacaoController {

    private final FichaAvaliacaoService fichaAvaliacaoService;

    public FichaAvaliacaoController(FichaAvaliacaoService fichaAvaliacaoService) {
        this.fichaAvaliacaoService = fichaAvaliacaoService;
    }

    @PostMapping("/criar")
    @ResponseStatus(HttpStatus.CREATED) // Retorna código 201
    @PreAuthorize("hasAnyROole('Instrutor','Gerente')") // Apenas instrutores e admins podem criar fichas de avaliação
    public FichaAvaliacaoResponse createFichaAvaliacao(@Valid @RequestBody FichaAvaliacaoRequest dto) {
        return  fichaAvaliacaoService.criar(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Instrutor','Gerente')")
    public List<FichaAvaliacaoResponse> getFichaAvaliacaoByIf(@PathVariable Long id) {
        return fichaAvaliacaoService.buscarPorId(id);
    }

    @GetMapping("/aluno/{alunoId}")
    @PreAuthorize("hasAnyRole('Instrutor','Gerente')")
    public List<FichaAvaliacaoResponse>  getFichasByAluno(@PathVariable Long id) {
        return fichaAvaliacaoService.buscarPorId(id);
    }
    // mono metodo para o aluno ver suas proprias avaliacoes
    @GetMapping("/minhas-avaliacoes")
    @PreAuthorize("hasRole('Aluno')")
    public List<FichaAvaliacaoResponse> listarMinhasAvaliacoes() {
        Usuario UsuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long alunoId = UsuarioAutenticado.getId();
        return fichaAvaliacaoService.buscarPorId(alunoId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Instrutor','Gerente')")
    public List<FichaAvaliacaoResponse> getAllFichas() {
        return fichaAvaliacaoService.listar();
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize(("hasAnyRole('Instrutor''Gerente')"))
    public FichaAvaliacaoResponse updateFichaAvaliacao(@PathVariable Long id, @Valid @RequestBody FichaAvaliacaoRequest dto) {
        return fichaAvaliacaoService.atualizar(id, dto);
    }

    @DeleteMapping("/excluir/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)// Retorna código 204
    @PreAuthorize(("hasRole('Gerente')")) // Apenas Gerente podem deletar
    public void deleteFichaAvalicao(@PathVariable Long id) {
        fichaAvaliacaoService.deletar(id);
    }







}
