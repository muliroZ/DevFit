package com.devfitcorp.devfit.dto;

import java.time.LocalDate;
import java.util.List;

public record FichaTreinoResponse(

        Long id,
        LocalDate datacriacao,

         // Relacionamentos usando DTOs Leves para segurança
        UsuarioInfoDTO aluno,
        UsuarioInfoDTO instrutor,

        LocalDate dataVencimento,
        boolean isAtiva,

        List<ItemTreinoResponse> ListaDeItens


) {
}
