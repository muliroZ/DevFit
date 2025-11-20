package com.devfitcorp.devfit.model;

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
@Table(name = "Fichas_treino") // nome da tabela no banco de dados
public class FichaTreino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno; // Relacionamento com o Aluno

    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private Usuario instrutor; // Relacionamento com o Instrutor que criou a ficha de treino

    private LocalDate dataCriacao;
    private LocalDate dataVencimento;   // Data de vencimento da ficha de treino
    private boolean isAtiva = true;    // Indica se a ficha de treino está ativa ou não

    @OneToMany(mappedBy = "fichaTreino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemTreino> listaDeItens = new ArrayList<>(); // Lista de itens de treino associados à ficha

    @PrePersist    // vai servir para preencher a data de criacao automaticamente
    public void prePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDate.now();
        }
    }




}



