package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.model.Plano;
import com.devfitcorp.devfit.service.PlanoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    private final PlanoService planoService;

    public PlanoController(PlanoService planoService) {
        this.planoService = planoService;
    }

    @GetMapping("/listar")
    public List<Plano> listarAtivos() {
        return planoService.listar();
    }
}
