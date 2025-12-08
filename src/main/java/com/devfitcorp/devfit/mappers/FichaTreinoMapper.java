package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.*;
import com.devfitcorp.devfit.model.Exercicio;
import com.devfitcorp.devfit.model.FichaTreino;
import com.devfitcorp.devfit.model.ItemTreino;
import com.devfitcorp.devfit.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FichaTreinoMapper {

    public FichaTreino toEntity(FichaTreinoRequest dto, Usuario aluno, Usuario instrutor, List<ItemTreino> itensTreino, LocalDate dataCriacao) {
        FichaTreino ficha = new FichaTreino();

        ficha.setAluno(aluno);
        ficha.setInstrutor(instrutor);
        ficha.setDataVencimento(dto.dataVencimento());
        ficha.setDataCriacao(dataCriacao);
        ficha.setAtiva(true);
        ficha.setListaDeItens(itensTreino);

        return ficha;
    }

    // Converte o DTO de ItemTreinoRequest para a Entidade ItemTreino
    public ItemTreino toEntity(ItemTreinoRequest dto, Exercicio exercicioBase) {
        ItemTreino item = new ItemTreino();

        item.setExercicioBase(exercicioBase);
        item.setSeries(dto.series());
        item.setRepeticoes(dto.repeticoes());
        item.setCargaEstimadaKg(dto.cargaEstimadaKg());
        item.setObservacoes(dto.observacoes());

        return item;
    }

    public FichaTreinoResponse toResponse(FichaTreino entity) {
        // Converte a lista aninhada (recursivamente)
        List<ItemTreinoResponse> itensResponse = entity.getListaDeItens().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        // Converte a Ficha
        return new FichaTreinoResponse(
                entity.getId(),
                toUsuarioInfoDTO(entity.getAluno()),
                toUsuarioInfoDTO(entity.getInstrutor()),
                entity.getDataCriacao(),
                entity.getDataVencimento(),
                entity.isAtiva(),
                itensResponse
        );
    }

    public ExercicioInfoDTO toExercicioInfoDTO(Exercicio exercicio) {
        return new ExercicioInfoDTO(
                exercicio.getId(),
                exercicio.getNome(),
                exercicio.getMusculoPrincipal()
        );
    }

    // Converte a Entidade ItemTreino para o DTO ItemTreinoResponse
    public ItemTreinoResponse toResponse(ItemTreino entity) {
        return new ItemTreinoResponse(
                entity.getId(),
                toExercicioInfoDTO(entity.getExercicioBase()),
                entity.getSeries(),
                entity.getRepeticoes(),
                entity.getCargaEstimadaKg(),
                entity.getObservacoes()
        );
    }

    // Converte a Entidade Usuario para o DTO UsuarioInfoDTO
    public UsuarioInfoDTO toUsuarioInfoDTO(Usuario entity) {
        return new UsuarioInfoDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail()
        );
    }
}
