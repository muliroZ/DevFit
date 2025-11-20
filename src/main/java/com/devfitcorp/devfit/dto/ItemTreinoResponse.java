package com.devfitcorp.devfit.dto;


public record ItemTreinoResponse(

        Long id,

        Long exercicioBaseId,
        String nomeExercicio,
        String musculoPrincipal,

        Integer series,
        Integer repeticoes,
        Double cargaEstimadaKg,
        String observacoes

) {
}
