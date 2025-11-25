package com.devfitcorp.devfit.dto;

import java.time.LocalDate;
import java.util.List;

public record FichaTreinoResponse(
        Long id,
         // Relacionamentos usando DTOs Leves para segurança
        UsuarioInfoDTO aluno,
        UsuarioInfoDTO instrutor,
        LocalDate dataCriacao,
        LocalDate dataVencimento,
        boolean isAtiva,
        List<ItemTreinoResponse> ListaDeItens
) {}
