package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "matriculas")
@NoArgsConstructor
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "plano_id")
    private Plano plano;


    private LocalDate dataVencimento;

    private boolean estaAtiva;




}