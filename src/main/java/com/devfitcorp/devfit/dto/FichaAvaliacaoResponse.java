package com.devfitcorp.devfit.dto;

import java.time.LocalDate;

public record FichaAvaliacaoResponse(
        // DADOS GERADOS E RELACIONAMENTOS
        Long id,
        // Usando DTOs Leves para Aluno e Instrutor (segurança)
        UsuarioInfoDTO aluno,
        UsuarioInfoDTO instrutor,
        LocalDate dataAvaliacao,
        Double pesoKg,
        Double alturaM,
        Double imc,
        Double circunferenciaCinturaCm,
        Double circunferenciaAbdomenCm,
        Double circunferenciaQuadrilCm,
        String historicoSaude,
        String observacoesGerais
) {}
