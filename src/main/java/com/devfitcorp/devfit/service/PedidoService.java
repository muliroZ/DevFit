package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.*;
import com.devfitcorp.devfit.exception.ProdutoNaoEncontradoException;
import com.devfitcorp.devfit.mappers.PedidoMapper;
import com.devfitcorp.devfit.model.ItemPedido;
import com.devfitcorp.devfit.model.Pedido;
import com.devfitcorp.devfit.model.Produto;
import com.devfitcorp.devfit.repository.PedidoRepository;
import com.devfitcorp.devfit.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper mapper;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         PedidoMapper mapper) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request) {

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(request.usuarioId());

        List<ItemPedido> itens = request.itens().stream().map(i -> {

            Produto produto = produtoRepository.findById(i.produtoId())
                    .orElseThrow(() -> new ProdutoNaoEncontradoException(i.produtoId()));

            if (produto.getEstoque() < i.quantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - i.quantidade());
            produtoRepository.save(produto);

            // Criar item
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(i.quantidade());

            // Preço de uma unidade de item
            item.setPrecoUnitario(produto.getPreco());

            // calculando preço total de todas as quantidades de um item
            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(i.quantidade()));
            item.setSubtotal(subtotal);

            return item;

        }).toList();

        pedido.setItens(itens);
        // soma do subtotal de cada um dos itens
        BigDecimal valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(valorTotal);

        return mapper.toResponse(pedidoRepository.save(pedido));
    }
}
