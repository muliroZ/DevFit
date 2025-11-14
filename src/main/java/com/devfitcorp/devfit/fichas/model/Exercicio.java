package com.devfitcorp.devfit.fichas.model;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Getter e @Setter geram os métodos getters e setters automaticamente.
@Getter
@Setter

@AllArgsConstructor // gera um construtor com todos os campos.

@NoArgsConstructor
@Embeddable       // indica que esta classe pode ser embutida em uma entidade JPA.
public class Exercicio {

    private String nome;
    private int series;
    private int repeticoes;
    private double cargaEstimadaKg; // carga em kg
    private String observacoes; // notas do instrutor, ex, "Fazer lento"
}
