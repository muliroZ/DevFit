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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Usuario instrutor;

    @Column(nullable = false)
    private LocalDate dataAvaliacao;


    @Column(nullable = false) private double pesoKg;
    @Column(nullable = false) private double alturaCm;
    @Column(nullable = false) private double imc;


    @Column private double circunferenciaCinturaCm;
    @Column private double circunferenciaAbdomenCm;
    @Column private double circunferenciaQuadrilCm;

    @Column(length = 1000, columnDefinition = "TEXT")
    private String historicoSaude;

    @Column(length = 500, columnDefinition = "TEXT")
    private String observacoesGerais;

    @PrePersist
    public void prePersist() {
        if (dataAvaliacao == null) {
            dataAvaliacao = LocalDate.now();
        }
    }
}
