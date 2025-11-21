package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "planos")
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Ex: 'Plano Mensal', 'Trimestral VIP'

    private BigDecimal valor; // O valor da mensalidade

    private Integer duracaoMeses; // Ex: 1, 3, 6, 12

    // [Adicione Construtores, Getters e Setters]
}