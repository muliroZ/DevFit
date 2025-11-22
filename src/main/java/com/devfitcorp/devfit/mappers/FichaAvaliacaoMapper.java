package com.devfitcorp.devfit.mappers;


import com.devfitcorp.devfit.dto.FichaAvaliacaoRequest;
import com.devfitcorp.devfit.dto.FichaAvaliacaoResponse;
import com.devfitcorp.devfit.model.FichaAvaliacao;
import com.devfitcorp.devfit.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class FichaAvaliacaoMapper {
    //  CAMPO NECESSÁRIO: Injeção do FichaTreinoMapper (para reuso do toUsuarioInfoDTO)
    private final FichaTreinoMapper fichaTreinoMapper;

    //  CONSTRUTOR NECESSÁRIO: Spring injeta o FichaTreinoMapper
    public FichaAvaliacaoMapper(FichaTreinoMapper fichaTreinoMapper) {
        this.fichaTreinoMapper = fichaTreinoMapper;
    }


    // --- CONVERSÃO DTO (Entrada) -> ENTIDADE ---
    public FichaAvaliacao toEntity(FichaAvaliacaoRequest dto, Usuario aluno, Usuario instrutor) {
        FichaAvaliacao ficha = new FichaAvaliacao();

        // Mapeamento dos objetos Usuario buscados pelo Service
        ficha.setAluno(aluno);
        ficha.setInstrutor(instrutor);

        // Mapeamento dos dados do Request para a Entidade
        ficha.setDataAvaliacao(dto.dataAvaliacao());
        ficha.setPesoKg(dto.pesoKg());
        ficha.setAlturaM(dto.alturaM());
        ficha.setCircunferenciaAbdomenCm(dto.circunferenciaAbdomenCm());
        ficha.setCircunferenciaCinturaCm(dto.circunferenciaCinturaCm());

        // CORREÇÃO: Adicionando o mapeamento do campo circunferência do quadril
        ficha.setCircunferenciaQuadrilCm(dto.circunferenciaQuadrilCm());

        ficha.setHistoricoSaude(dto.historicoSaude());
        ficha.setObservacoesGerais(dto.observacoesGerais());

        return ficha;
    }


    public FichaAvaliacaoResponse toResponse(FichaAvaliacao entity) {
        return new FichaAvaliacaoResponse(
                entity.getId(),
                entity.getDataAvaliacao(),
                // Reuso do método do FichaTreinoMapper para converter os usuários
                fichaTreinoMapper.toUsuarioInfoDTO(entity.getAluno()),
                fichaTreinoMapper.toUsuarioInfoDTO(entity.getInstrutor()),
                entity.getPesoKg(),
                entity.getAlturaM(),
                entity.getImc(), // O IMC calculado na camada Service
                entity.getCircunferenciaCinturaCm(),
                entity.getCircunferenciaAbdomenCm(),
                entity.getCircunferenciaQuadrilCm(),
                entity.getHistoricoSaude(),
                entity.getObservacoesGerais()
        );
    }
}

