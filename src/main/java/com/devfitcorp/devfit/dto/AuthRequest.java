package com.devfitcorp.devfit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
        @NotNull(message = "O nome de usuário é obrigatório") String nome,
        @NotNull(message = "A senha é obrigatória") String senha,
        @NotNull(message = "O email é obrigatório") @Valid String email
) {}
