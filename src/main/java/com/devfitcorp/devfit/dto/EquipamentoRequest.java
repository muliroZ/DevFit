package com.devfitcorp.devfit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquipamentoRequest(
        String nome,
        String descricao,
        Integer quantidade,
        BigDecimal valor,
        LocalDate dataAquisicao,
        String status
) {}