package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.*;
import com.devfitcorp.devfit.model.ItemPedido;
import com.devfitcorp.devfit.model.Pedido;
import com.devfitcorp.devfit.model.Produto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class PedidoMapper {

    public ItemPedido toEntity(ItemPedidoRequest dto, Produto produto, Pedido pedido) {
        ItemPedido item = new ItemPedido();

        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(dto.quantidade());
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco()
                .multiply(BigDecimal.valueOf(dto.quantidade())));

        return item;
    }

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
                pedido.getUsuario().getId(),
                pedido.getDataCriacao(),
                pedido.getItens().stream()
                        .map(this::toItemResponse)
                        .toList(),
                pedido.getValorTotal()
        );
    }
}