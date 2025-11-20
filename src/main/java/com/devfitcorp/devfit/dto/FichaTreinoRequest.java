package com.devfitcorp.devfit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record FichaTreinoRequest(

        //  Relacionamentos (Apenas IDs dos Usuários são enviados)
        @NotNull(message = "O ID do aluno é obrigatório.")
        Long alunoId,

        @NotNull(message = "O ID do instrutor é obrigatório.")
        Long instrutorId,

        //  Validação de Data
        @NotNull(message = "A data de vencimento é obrigatória.")
        @FutureOrPresent(message = "A data de vencimento deve ser hoje ou futura.")
        LocalDate dataVencimento,

        //  Validação da Lista de Itens de Treino (Composição)
        @NotNull(message = "A ficha deve conter a lista de itens de treino.")
        @Size(min = 1, message = "A ficha deve ter pelo menos 1 item de treino.")
        @Valid // Força a validação recursiva em cada ItemTreinoRequest
        List<ItemTreinoRequest> listaDeItens

) {
}
