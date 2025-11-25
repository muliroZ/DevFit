package com.devfitcorp.devfit.dto;

public record ItemTreinoResponse(
        Long id,
        ExercicioInfoDTO exercicio,
        Integer series,
        Integer repeticoes,
        Double cargaEstimadaKg,
        String observacoes
) {}
