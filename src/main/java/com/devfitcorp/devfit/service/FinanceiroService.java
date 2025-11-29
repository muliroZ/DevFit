package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;
import com.devfitcorp.devfit.repository.MensalidadeRepository;
import com.devfitcorp.devfit.repository.DespesaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Service
public class FinanceiroService {

    private final MensalidadeRepository mensalidadeRepository;
    private final DespesaRepository despesaRepository;
    private final ProdutoRepository produtoRepository;

    public FinanceiroService(MensalidadeRepository mensalidadeRepository,
                             DespesaRepository despesaRepository,
                             ProdutoRepository produtoRepository){
        this.mensalidadeRepository = mensalidadeRepository;
        this.despesaRepository = despesaRepository;
    }

    public FinanceiroDashboardDTO getAggregatedFinanceiroSummary(){

        LocalDate hoje  = LocalDate.now();
        LocalDate primeiroDiaMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate ultimoDiaMes = hoje.with(TemporalAdjusters.lastDayOfMonth());


        BigDecimal receitaMensalidades = mensalidadeRepository.sumValorPagoByPeriodo(primeiroDiaMes, ultimoDiaMes);
        BigDecimal receitaProduto = produtoRepository.sumValorTotalByPeriodo(primeiroDiaDoMes, ultimoDiaDoMes);

        BigDecimal receitaTotal = receitaMensalidades.add(receitaProduto);


        BigDecimal despesaSalarios = despesaRepository.sumValorByCategoriaAndPeriodo("SALARIOS", primeiroDiaMes, ultimoDiaMes);
        BigDecimal despesaEquipamentos = despesaRepository.sumValorByCategoriaAndPeriodo("EQUIPAMENTOS", primeiroDiaMes, ultimoDiaMes);
        BigDecimal despesaAluguel = despesaRepository.sumValorByCategoriaAndPeriodo("ALUGUEL", primeiroDiaMes, ultimoDiaMes);

        BigDecimal despesaTotal = despesaSalarios.add(despesaEquipamentos).add(despesaAluguel);


        FinanceiroDashboardDTO dto = new FinanceiroDashboardDTO();

        dto.setReceitaTotal(receitaTotal);
        dto.setDespesaTotal(despesaTotal);
        dto.setLucroLiquido(receitaTotal.subtract(despesaTotal));


        Map<String,BigDecimal> receitaPorFonte = new HashMap<>();
        receitaPorFonte.put("Mensalidades", receitaMensalidades);
        receitaPorFonte.put("Produto", receitaProduto);
        dto.setReceitaPorFonte(receitaPorFonte);

        if (receitaProduto.compareTo(receitaTotal) == 0) {
            receitaPorFonte.put("Produto", receitaTotal);
        }
        dto.setReceitaPorFonte(receitaPorFonte);


        Map<String, BigDecimal> despesaPorCategoria = new HashMap<>();
        if (despesaSalarios.compareTo(BigDecimal.ZERO) > 0) {
            despesaPorCategoria.put("Salários", despesaSalarios);
        }
        if (despesaEquipamentos.compareTo(BigDecimal.ZERO) > 0) {
            despesaPorCategoria.put("Equipamentos", despesaEquipamentos);
        }
        if (despesaAluguel.compareTo(BigDecimal.ZERO) > 0) {
            despesaPorCategoria.put("Aluguel", despesaAluguel);
        }
        dto.setDespesaPorCategoria(despesaPorCategoria);

        return dto;
    }
}
