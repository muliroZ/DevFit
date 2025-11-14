package com.devfitcorp.devfit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
@NotBlank
@Size(min = 1, max = 100)
private String nome;

@Size(max = 500)
private String descricao;

@NotNull
@Positive
private Double preco;

@NotNull
@PositiveOrZero
private Integer estoque;
}
