package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequest(

        @NotBlank
        @Size(min = 1, max = 100)
        String nome,

        @Size(max = 500)
        String descricao,

        @NotNull
        @PositiveOrZero
        BigDecimal preco,

        @NotNull
        @PositiveOrZero
        Integer estoque

) {}
