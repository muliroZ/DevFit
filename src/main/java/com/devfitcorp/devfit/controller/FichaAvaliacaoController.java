package com.devfitcorp.devfit.controller;


import com.devfitcorp.devfit.dto.FichaAvaliacaoRequest;
import com.devfitcorp.devfit.dto.FichaAvaliacaoResponse;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.service.FichaAvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fichas/avaliacao")
public class FichaAvaliacaoController {

    private final FichaAvaliacaoService fichaAvaliacaoService;

    public FichaAvaliacaoController(FichaAvaliacaoService fichaAvaliacaoService) {
        this.fichaAvaliacaoService = fichaAvaliacaoService;
    }

    @PostMapping("/criar")
    @PreAuthorize("hasAnyRole('INSTRUTOR','GESTOR')")
    public ResponseEntity<FichaAvaliacaoResponse> criarFichaAvaliacao(@Valid @RequestBody FichaAvaliacaoRequest dto) {
        FichaAvaliacaoResponse response = fichaAvaliacaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUTOR','GESTOR')")
    public ResponseEntity<FichaAvaliacaoResponse> buscarFichaAvaliacaoPorId(@PathVariable Long id) {
        FichaAvaliacaoResponse response = fichaAvaliacaoService.buscarFichasPorId(id).stream().findFirst().orElse(null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/aluno/{alunoId}")
    @PreAuthorize("hasAnyRole('INSTRUTOR','GESTOR')")
    public ResponseEntity<List<FichaAvaliacaoResponse>>  buscarFichasPorAluno(@PathVariable Long alunoId) {

        List<FichaAvaliacaoResponse> response = fichaAvaliacaoService.buscarFichasPorId(alunoId);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('INSTRUTOR','GESTOR')")
    public ResponseEntity<List<FichaAvaliacaoResponse>> buscarTodasAsFichas() {
        return  ResponseEntity.ok(fichaAvaliacaoService.listar());
    }

    @GetMapping("/minhas-avaliacoes")
    @PreAuthorize("hasAnyRole('ALUNO', 'INSTRUTOR', 'GESTOR')")
    public ResponseEntity<List<FichaAvaliacaoResponse>> listarMinhasAvaliacoes() {
        Usuario UsuarioAutenticado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long alunoId = UsuarioAutenticado.getId();

        List<FichaAvaliacaoResponse> lista = fichaAvaliacaoService.buscarFichasPorId(alunoId);
        return ResponseEntity.ok(lista);

    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize(("hasAnyRole('INSTRUTOR','GESTOR')"))
    public ResponseEntity<FichaAvaliacaoResponse> atualizarFichaAvaliacao(@PathVariable Long id, @Valid @RequestBody FichaAvaliacaoRequest dto) {
        FichaAvaliacaoResponse response = fichaAvaliacaoService.atualizar(id, dto);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/excluir/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)// Retorna código 204
    @PreAuthorize(("hasRole('GESTOR')")) // Apenas Gerente podem deletar
    public void deletarFichaAvalicao(@PathVariable Long id) {
        fichaAvaliacaoService.deletar(id);
    }







}
