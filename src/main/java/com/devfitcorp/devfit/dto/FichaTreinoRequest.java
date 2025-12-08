package com.devfitcorp.devfit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record FichaTreinoRequest(

        @NotNull(message = "O id do aluno é obrigatório.")
        Long idAluno,

        @NotNull(message = "O id do instrutor é obrigatório.")
        Long idInstrutor,

        @NotNull(message = "A data de vencimento é obrigatória.")
        @FutureOrPresent(message = "A data de vencimento deve ser hoje ou futura.")
        LocalDate dataVencimento,

        @NotNull(message = "A ficha deve conter a lista de itens de treino.")
        @Size(min = 1, message = "A ficha deve ter pelo menos 1 item de treino.")
        @Valid
        List<ItemTreinoRequest> listaDeItens
) {}
