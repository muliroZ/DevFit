package com.devfitcorp.devfit.dto;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
