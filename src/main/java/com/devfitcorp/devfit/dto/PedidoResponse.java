package com.devfitcorp.devfit.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long usuarioId,
        LocalDateTime dataCriacao,
        List<ItemPedidoResponse> itens,
        BigDecimal valorTotal
) {}
