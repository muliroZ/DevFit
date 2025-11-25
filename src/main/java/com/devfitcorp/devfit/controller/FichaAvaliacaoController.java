package com.devfitcorp.devfit.controller;


import com.devfitcorp.devfit.dto.FichaAvaliacaoRequest;
import com.devfitcorp.devfit.dto.FichaAvaliacaoResponse;
import com.devfitcorp.devfit.service.FichaAvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public FichaAvaliacaoResponse createFichaAvaliacao(@Valid @RequestBody FichaAvaliacaoRequest dto) {
        return  fichaAvaliacaoService.criar(dto);
    }

    @GetMapping("/{id}")
    public List<FichaAvaliacaoResponse> getFichaAvaliacaoByIf(@PathVariable Long id) {
        return fichaAvaliacaoService.buscarPorId(id);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<FichaAvaliacaoResponse>  getFichasByAluno(@PathVariable Long id) {
        return fichaAvaliacaoService.buscarPorId(id);
    }

    @GetMapping
    public List<FichaAvaliacaoResponse> getAllFichas() {
        return fichaAvaliacaoService.listar();
    }

    @PutMapping("/atualizar/{id}")
    public FichaAvaliacaoResponse updateFichaAvaliacao(@PathVariable Long id, @Valid @RequestBody FichaAvaliacaoRequest dto) {
        return fichaAvaliacaoService.atualizar(id, dto);
    }
    @DeleteMapping("/excluir/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna código 204
    public void deleteFichaAvalicao(@PathVariable Long id) {
        fichaAvaliacaoService.deletar(id);
    }







}
