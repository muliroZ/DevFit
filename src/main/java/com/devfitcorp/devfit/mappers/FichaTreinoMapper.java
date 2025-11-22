package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.UsuarioInfoDTO;
import com.devfitcorp.devfit.dto.FichaTreinoRequest;
import com.devfitcorp.devfit.dto.FichaTreinoResponse;
import com.devfitcorp.devfit.dto.ItemTreinoRequest;
import com.devfitcorp.devfit.dto.ItemTreinoResponse;
import com.devfitcorp.devfit.model.Exercicio;
import com.devfitcorp.devfit.model.FichaTreino;
import com.devfitcorp.devfit.model.ItemTreino;
import com.devfitcorp.devfit.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component // O Spring gerencia esta classe e a injeta no Service.
public class FichaTreinoMapper {

    // Converte o DTO de Ficha para a Entidade FichaTreino
    public FichaTreino toEntity(FichaTreinoRequest dto, Usuario aluno, Usuario instrutor, List<ItemTreino> itensTreino) {
        FichaTreino ficha = new FichaTreino();

        ficha.setAluno(aluno);
        ficha.setInstrutor(instrutor);

        ficha.setDataVencimento(dto.dataVencimento());
        ficha.setAtiva(true);

        ficha.setListaDeItens(itensTreino);

        return ficha;

    }
    // Converte o DTO de item para a Entidade ItemTreino
    public ItemTreino toEntity(ItemTreinoRequest dto, Exercicio execicioBase) {
        ItemTreino item = new ItemTreino();

        item.setExercicioBase(execicioBase);

        item.setSeries(dto.series());
        item.setRepeticoes(dto.repeticoes());
        item.setCargaEstimadaKg(dto.cargaEstimadaKg());
        item.setObservacoes(dto.observacoes());

        return item;
    }

    public FichaTreinoResponse toResponse(FichaTreino entity) {

        // 1. Converte a lista aninhada (recursivamente)
        List<ItemTreinoResponse> itensResponse = entity.getListaDeItens().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        // 2. Converte a Ficha
        return new FichaTreinoResponse(
                entity.getId(),
                entity.getDataCriacao(),
                toUsuarioInfoDTO(entity.getAluno()),
                toUsuarioInfoDTO(entity.getInstrutor()),
                entity.getDataVencimento(),
                entity.isAtiva(),
                itensResponse
        );
    }
     // Converte a Entidade ItemTreino para o DTO ItemTreinoResponse
    public ItemTreinoResponse toResponse(ItemTreino entity) {
        return new ItemTreinoResponse(
                entity.getId(),
                entity.getExercicioBase().getId(),
                entity.getExercicioBase().getNome(),
                entity.getExercicioBase().getMusculoPrincipal(),
                entity.getSeries(),
                entity.getRepeticoes(),
                entity.getCargaEstimadaKg(),
                entity.getObservacoes()
        );
    }
    // Converte a Entidade Usuario para o DTO UsuarioInfoDTO
    private UsuarioInfoDTO toUsuarioInfoDTO(Usuario entity) {
        return new UsuarioInfoDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail()
        );
    }



}
