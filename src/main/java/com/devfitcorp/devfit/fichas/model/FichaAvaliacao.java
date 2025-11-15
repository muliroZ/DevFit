package com.devfitcorp.devfit.fichas.model;


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
@Table(name = "FichaAvaliacao")
public class FichaAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID da Ficha de Avaliação (Chave Primária)

    private Long alunoId; // Relacionamento com o Aluno
    private Long instrutorId; // Relacionamento com o Instrutor (quem realizou a avaliação)
    private LocalDate dataAvaliacao = LocalDate.now();

    // Dados Antropométricos
    private double pesoKg;
    private double alturaM; // Em metros
    private double imc; // Índice de Massa Corporal (calculado)

    // Circunferências
    private double circunferenciaCinturaCm;
    private double circunferenciaAbdomenCm;
    private double circunferenciaQuadrilCm;

    // Histórico e Observações
    private String historicoSaude; // Informações prévias relevantes
    private String observacoesGerais;


    // metodo para calcular o IMC. Se a altura for inválida, lança uma exceção
    public double calcularIMC() {
        if (alturaM <= 0) {
            throw new IllegalArgumentException("Altura deve ser um valor positivo para calcular o IMC.");
        }
        this.imc = this.pesoKg / (this.alturaM * this.alturaM);
        return this.imc;
    }
}
