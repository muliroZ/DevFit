package com.devfitcorp.devfit.dto;

import com.devfitcorp.devfit.model.Equipamento;
import java.math.BigDecimal;
import java.time.LocalDate;


public record EquipamentoDashboardDTO(
        Long id,
        String nome,
        String descricao,
        Integer quantidade,
        BigDecimal valor,
        LocalDate dataAquisao
) {
    public static  EquipamentoDashboardDTO fromEntity(Equipamento equipamento) {
        return new EquipamentoDashboardDTO(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getDescricao(),
                equipamento.getQuantidade(),
                equipamento.getValor(),
                equipamento.getDataAquisicao()
        );
    }
}
