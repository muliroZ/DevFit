package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.*;
import com.devfitcorp.devfit.model.ItemPedido;
import com.devfitcorp.devfit.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {

    public ItemPedidoResponse toItemResponse(ItemPedido item) {
        return new ItemPedidoResponse(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }

    public PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getDataCriacao(),
                pedido.getItens()
                        .stream()
                        .map(this::toItemResponse)
                        .toList(),
                pedido.getValorTotal()
        );
    }
}
