package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ficha_avaliacao")
public class FichaAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID da Ficha de Avaliação (Chave Primária)

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno; // Relacionamento com o Aluno

    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Usuario instrutor; // Relacionamento com o Instrutor que criou a ficha de avaliação

    @Column(nullable = false)
    private LocalDate dataAvaliacao; // Data da Avaliação

    // Dados Antropométricos
    @Column(nullable = false) private double pesoKg;
    @Column(nullable = false) private double alturaCm; // Em centímetros
    private double imc; // Índice de Massa Corporal (calculado)

    // Circunferências
    @Column private double circunferenciaCinturaCm;
    @Column private double circunferenciaAbdomenCm;
    @Column private double circunferenciaQuadrilCm;

    // Histórico e Observações
    @Column(length = 1000, columnDefinition = "TEXT")
    private String historicoSaude; // Informações prévias relevantes

    @Column(length = 500, columnDefinition = "TEXT")
    private String observacoesGerais;

    @PrePersist
    public void prePersist() {
        if (dataAvaliacao == null) {
            dataAvaliacao = LocalDate.now();
        }
    }
}
