package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.*;

public record FichaAvaliacaoRequest(
        @NotNull(message = "O id do aluno é obrigatório.")
        Long idAluno,

        @NotNull(message = "O id do instrutor é obrigatório.")
        Long idInstrutor,

        @NotNull(message = "O peso é obrigatório.")
        @Positive(message = "O peso deve ser um valor positivo.")
        Double pesoKg,

        @NotNull(message = "A altura é obrigatória.")
        @Positive(message = "A altura deve ser um valor positivo.")
        Double alturaCm,

        @Positive(message = "O valor da cintura deve ser positivo.")
        Double circunferenciaCinturaCm,

        @Positive(message = "O valor do abdômen deve ser positivo.")
        Double circunferenciaAbdomenCm,

        @Positive(message = "O valor do quadril deve ser positivo.")
        Double circunferenciaQuadrilCm,

        @Size(max = 1000, message = "O histórico de saúde não pode exceder 1000 caracteres.")
        String historicoSaude,

        @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
        String observacoesGerais
) {}
