package com.devfitcorp.devfit.repository;

import com.devfitcorp.devfit.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}