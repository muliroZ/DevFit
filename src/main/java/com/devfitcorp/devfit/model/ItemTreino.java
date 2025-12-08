package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_treino")
public class ItemTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int series;

    @Column(nullable = false)
    private int repeticoes;

    @Column
    private double cargaEstimadaKg;

    @Column(length = 500, columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficha_treino_id", nullable = false)
    private FichaTreino fichaTreino;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercicio_base_id", nullable = false)
    private Exercicio exercicioBase;
}
