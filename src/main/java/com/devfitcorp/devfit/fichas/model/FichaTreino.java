package com.devfitcorp.devfit.fichas.model;

import jakarta.persistence.*;
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
@Entity   // entidade JPA para persistência no banco de dados
@Table(name = "Fichatreino") // nome da tabela no banco de dados
public class FichaTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária

    private Long alunoId;
    private Long InstrutorId;
    private LocalDate dataCriacao = LocalDate.now();   // Data de criação da ficha de treino
    private LocalDate dataVencimento;   // Data de vencimento da ficha de treino
    private boolean isAtiva = true;    // Indica se a ficha de treino está ativa ou não

    // Mapeamento da lista de exercícios como uma coleção embutida
    @ElementCollection
    // Define o nome da tabela e a coluna de junção para a coleção de exercícios
    @CollectionTable(name = "FichaTreino_Exercicios", joinColumns = @JoinColumn(name = "ficha_treino_id"))
    private List<Exercicio> listaDeExercicios = new ArrayList<>(); // Lista de exercícios na ficha de treino



}



