package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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

    private boolean ativo;

    @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matricula> matriculas;
}