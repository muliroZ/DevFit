package com.devfitcorp.devfit.mappers;

import com.devfitcorp.devfit.dto.FichaAvaliacaoRequest;
import com.devfitcorp.devfit.dto.FichaAvaliacaoResponse;
import com.devfitcorp.devfit.model.FichaAvaliacao;
import com.devfitcorp.devfit.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FichaAvaliacaoMapper {
    //   Injeção do FichaTreinoMapper (para reuso do toUsuarioInfoDTO)
    private final FichaTreinoMapper fichaTreinoMapper;


    public FichaAvaliacaoMapper(FichaTreinoMapper fichaTreinoMapper) {
        this.fichaTreinoMapper = fichaTreinoMapper;
    }

    public FichaAvaliacao toEntity(FichaAvaliacaoRequest dto, Usuario aluno, Usuario instrutor, LocalDate dataAvaliacao) {
        FichaAvaliacao ficha = new FichaAvaliacao();

        ficha.setAluno(aluno);
        ficha.setInstrutor(instrutor);
        ficha.setDataAvaliacao(dataAvaliacao);
        ficha.setPesoKg(dto.pesoKg());
        ficha.setAlturaCm(dto.alturaCm());
        ficha.setCircunferenciaAbdomenCm(dto.circunferenciaAbdomenCm());
        ficha.setCircunferenciaCinturaCm(dto.circunferenciaCinturaCm());
        ficha.setCircunferenciaQuadrilCm(dto.circunferenciaQuadrilCm());
        ficha.setHistoricoSaude(dto.historicoSaude());
        ficha.setObservacoesGerais(dto.observacoesGerais());

        return ficha;
    }


    public FichaAvaliacaoResponse toResponse(FichaAvaliacao entity) {
        return new FichaAvaliacaoResponse(
                entity.getId(),
                fichaTreinoMapper.toUsuarioInfoDTO(entity.getAluno()),
                fichaTreinoMapper.toUsuarioInfoDTO(entity.getInstrutor()),
                entity.getDataAvaliacao(),
                entity.getPesoKg(),
                entity.getAlturaCm(),
                entity.getImc(), // O IMC calculado na camada Service
                entity.getCircunferenciaCinturaCm(),
                entity.getCircunferenciaAbdomenCm(),
                entity.getCircunferenciaQuadrilCm(),
                entity.getHistoricoSaude(),
                entity.getObservacoesGerais()
        );
    }
}

