package com.devfitcorp.devfit.dto;

import java.time.LocalDate;

public record FichaAvalicaoResponse(
        // DADOS GERADOS E RELACIONAMENTOS
        Long id,
        LocalDate dataAvaliacao,

        // Usando DTOs Leves para Aluno e Instrutor (segurança)
        UsuarioInfoDTO aluno,
        UsuarioInfoDTO instrutor,

        Double pesoKg,
        Double alturaM,

        Double imc,

        Double circunferenciaCinturaCm,
        Double circunferenciaAbdomenCm,
        Double circunferenciaQuadrilCm,
        String historicoSaude,
        String observacoesGerais
) {
}
