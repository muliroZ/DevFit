package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "O email é obrigatório") String email,
        @NotNull(message = "A senha é obrigatória") String senha
) {}
