package com.devfitcorp.devfit.dto;

import java.util.List;

public record PedidoRequest(
        Long usuarioId,
        List<ItemPedidoRequest> itens
) {}
