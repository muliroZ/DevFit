package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.*;
import com.devfitcorp.devfit.exception.ProdutoNaoEncontradoException;
import com.devfitcorp.devfit.exception.UsuarioNaoEncontradoException;
import com.devfitcorp.devfit.mappers.PedidoMapper;
import com.devfitcorp.devfit.model.ItemPedido;
import com.devfitcorp.devfit.model.Pedido;
import com.devfitcorp.devfit.model.Produto;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.PedidoRepository;
import com.devfitcorp.devfit.repository.ProdutoRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoMapper mapper;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         UsuarioRepository usuarioRepository,
                         PedidoMapper mapper) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request) {

        Pedido pedido = new Pedido();

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(request.usuarioId()));

        pedido.setUsuario(usuario);

        List<ItemPedido> itens = request.itens().stream().map(i -> {

            Produto produto = produtoRepository.findById(i.produtoId())
                    .orElseThrow(() -> new ProdutoNaoEncontradoException(i.produtoId()));

            if (produto.getEstoque() < i.quantidade()) {
                throw new RuntimeException(
                        "Estoque insuficiente para o produto: " + produto.getNome()
                );
            }

            produto.setEstoque(produto.getEstoque() - i.quantidade());
            produtoRepository.save(produto);

            return mapper.toEntity(i, produto, pedido);

        }).toList();

        pedido.setItens(itens);
        BigDecimal valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(valorTotal);

        return mapper.toResponse(pedidoRepository.save(pedido));
    }
}
