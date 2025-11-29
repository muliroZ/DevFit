package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemTreinoRequest(
        @NotNull(message = "O exercício base é obrigatório.")
        Long exercicioId,

        @NotNull(message = "O número de séries é obrigatório.")
        @Min(value = 1, message = "O número de séries deve ser no mínimo 1.")
        Integer series,

        @NotNull(message = "O número de repetições é obrigatório.")
        @Min(value = 4, message = "O número de repetições deve ser no mínimo 4.")
        Integer repeticoes,

        @Positive(message = "A carga estimada deve ser um valor positivo.")
        Double cargaEstimadaKg,

        @Size(max = 500, message = "As observações não podem exceder 500 caracteres.")
        String observacoes
) {}
