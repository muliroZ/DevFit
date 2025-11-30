package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.PedidoRequest;
import com.devfitcorp.devfit.dto.PedidoResponse;
import com.devfitcorp.devfit.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@RequestBody @Valid PedidoRequest request) {
        return ResponseEntity.status(201).body(service.criarPedido(request));
    }
}