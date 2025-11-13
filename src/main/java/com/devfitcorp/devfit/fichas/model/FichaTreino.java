package com.devfitcorp.devfit.fichas.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FichaTreino {

    private Long id; // Identificador único da ficha de treino
    private Long alunoId; // Relaciomaneto: ID do aluno (CRUD-1)
    private LocalDate dataCriacao; // Data de criação da ficha de treino
    private LocalDate dataVencimento; // Data de vencimento da ficha de treino
    private boolean isAtiva; // Indica se a ficha de treino está ativa ou não

     // uma ficha de treino pode ter varios exercicios
    private List<Exercicio> listaDeExercicios = new ArrayList<>(); // Lista de exercícios na ficha de treino

    // adicionei um construtor de dominio para facilicar a criacao da ficha de treino
    public FichaTreino(Long alunoId, LocalDate dataCriacao, LocalDate dataVencimento, List<Exercicio> exercicios) {
        this.alunoId = alunoId;
        this.dataCriacao = LocalDate.now(); // Data de criacao é sempre hoje
        this.dataVencimento = dataVencimento;
        this.isAtiva = true; // Por padrão, a ficha é criada como ativa
        this.listaDeExercicios = exercicios;
    }

    public  void adicionarExercicio(Exercicio exercicio) {
        // uma verificação simples para evitar adicionar exercicios nulos
        if (exercicio != null) {
            this.listaDeExercicios.add(exercicio);
        }


    }




}
