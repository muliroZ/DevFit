package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.EquipamentoDashboardDTO;
import com.devfitcorp.devfit.dto.EquipamentoRequest;
import com.devfitcorp.devfit.model.Equipamento;
import com.devfitcorp.devfit.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dashboard/equipamentos")
public class EquipamentoController {
    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('GESTOR')")
    public ResponseEntity<List<EquipamentoDashboardDTO>> listarEquipamentos() {
        List<EquipamentoDashboardDTO> lista = equipamentoService.listarTodosParaDashboard();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/criar")
    public ResponseEntity<Equipamento> criar(@RequestBody @Valid EquipamentoRequest request) {
        Equipamento novoEquipamento = equipamentoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEquipamento);
    }
}
