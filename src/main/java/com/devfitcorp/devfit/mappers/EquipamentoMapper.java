package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.EquipamentoRequest;
import com.devfitcorp.devfit.model.Equipamento;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {

    public Equipamento toEntity(EquipamentoRequest request) {
        Equipamento equipamento = new Equipamento();
        equipamento.setNome(request.nome());
        equipamento.setDescricao(request.descricao());
        equipamento.setQuantidade(request.quantidade());
        equipamento.setValor(request.valor());
        equipamento.setDataAquisicao(request.dataAquisicao());
        equipamento.setStatus(request.status());
        return equipamento;
    }
}
