package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.NotBlank;

public record ExercicioRequest(
        @NotBlank(message = "O nome é obrigatório") String nome,
        @NotBlank(message = "O grupo muscular é obrigatório") String musculoPrincipal,
        String descricao
) {}