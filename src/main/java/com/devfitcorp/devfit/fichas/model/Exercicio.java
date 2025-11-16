package com.devfitcorp.devfit.fichas.model;


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
    private Long id; // ID do exercicio

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String musculoPrincipal; //Ex: Peito, Costas, Pernas

    @Column(length = 1000) // sera o tamnanho da descricão
    private String descricao; // descrição do exercício

}
