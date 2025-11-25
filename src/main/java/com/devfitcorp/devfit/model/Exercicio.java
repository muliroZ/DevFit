package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Getter e @Setter geram os métodos getters e setters automaticamente.
@Getter
@Setter
@AllArgsConstructor // gera um construtor com todos os campos.
@NoArgsConstructor
@Entity    // entidade JPA para persistência no banco de dados
public class Exercicio {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id; // ID do exercício

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String musculoPrincipal; // Ex: Peito, Costas, Quadríceps

    @Column(length = 1000, columnDefinition = "TEXT") // será o tamanho da descrição
    private String descricao; // descrição do exercício
}
