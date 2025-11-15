package com.devfitcorp.devfit.dto;


import com.devfitcorp.devfit.fichas.model.Exercicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FichaTreinoRequest {

     // Dados do Aluno
    @NotNull(message = "O ID do aluno é obrigatório")
    @Min(value = 1, message = "O ID do aluno deve ser positivo")
    private Long alunoId;
    
     // Dados do Instrutor
    @NotNull(message = "O ID do instrutor é obrigatório")
    @Min(value = 1, message = "O ID do instrutor deve ser positivo.")
    private Long instrutorId;

     // Data de criação da ficha de treino
    @FutureOrPresent(message = "A data de criação da ficha deve ser hoje ou futura")
    private LocalDate dataVencimento;

     // Validação da lista de exercícios
    @NotNull(message = "A ficha deve conter a lista de exercicios ")
    @Size(min = 1, message = "A ficha deve conter pelo menos um exercício")
    @Valid  // Garante que as regras de validação dentro de Exercicio também sejam aplicadas
    private List<Exercicio> listaDeExercicios;

}
