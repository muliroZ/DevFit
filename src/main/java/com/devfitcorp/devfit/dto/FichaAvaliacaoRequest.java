package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record FichaAvaliacaoRequest(

        @NotNull(message = "O email do aluno é obrigatório.")
        @Email(message = "O email do aluno deve ser válido.")
        String emailAluno,

        @NotNull(message = "O email do instrutor é obrigatório.")
        @Email(message = "O email do instrutor deve ser válido.")
        String emailInstrutor,

        @FutureOrPresent(message = "A data de avaliação não pode ser futura.")
        LocalDate dataAvaliacao,

        @NotNull(message = "O peso é obrigatório.")
        @Positive(message = "O peso deve ser um valor positivo.")
        Double pesoKg,

        @NotNull(message = "A altura é obrigatória.")
        @Positive(message = "A altura deve ser um valor positivo.")
        Double alturaM,

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
) {
}
