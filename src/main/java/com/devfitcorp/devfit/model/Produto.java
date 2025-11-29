package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    @Column(nullable = false, unique = true)
    private String nome;

    @Size(max = 500)
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal preco;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer estoque;

    @Column(length = 500, columnDefinition = "TEXT")
    private String imagemUrl;
}
