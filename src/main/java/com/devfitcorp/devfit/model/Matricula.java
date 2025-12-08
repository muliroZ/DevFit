package com.devfitcorp.devfit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    @JsonIgnore
    private Plano plano;

    @OneToOne(mappedBy = "matricula")
    private Usuario usuario;

    private LocalDate dataInicio;

    private LocalDate dataVencimento;

    private boolean isAtiva;

    @PrePersist
    protected void onCreate() {
        this.dataInicio = LocalDate.now();
    }
}