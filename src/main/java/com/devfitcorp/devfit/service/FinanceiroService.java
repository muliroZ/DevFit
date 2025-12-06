package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;
import com.devfitcorp.devfit.repository.MensalidadeRepository;
import com.devfitcorp.devfit.repository.DespesaRepository;
import com.devfitcorp.devfit.repository.PedidoRepository;
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
    private final PedidoRepository pedidoRepository;

    public FinanceiroService(
            MensalidadeRepository mensalidadeRepository,
            DespesaRepository despesaRepository,
            PedidoRepository pedidoRepository
    ){
        this.mensalidadeRepository = mensalidadeRepository;
        this.despesaRepository = despesaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public FinanceiroDashboardDTO getAggregatedFinanceiroSummary(){

        LocalDate hoje  = LocalDate.now();
        LocalDate primeiroDiaMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate ultimoDiaMes = hoje.with(TemporalAdjusters.lastDayOfMonth());

        BigDecimal receitaMensalidades = mensalidadeRepository.sumValorPagoByPeriodo(primeiroDiaMes, ultimoDiaMes);
        receitaMensalidades = receitaMensalidades == null ? BigDecimal.ZERO : receitaMensalidades;

        BigDecimal receitaProduto = pedidoRepository.sumValorTotalByPeriodo(primeiroDiaMes, ultimoDiaMes);
        receitaProduto = receitaProduto == null ? BigDecimal.ZERO : receitaProduto;

        BigDecimal receitaTotal = receitaMensalidades.add(receitaProduto);

        BigDecimal despesaSalarios = despesaRepository.sumValorByCategoriaAndPeriodo("SALARIOS", primeiroDiaMes, ultimoDiaMes);
        despesaSalarios = despesaSalarios == null ? BigDecimal.ZERO : despesaSalarios;

        BigDecimal despesaEquipamentos = despesaRepository.sumValorByCategoriaAndPeriodo("EQUIPAMENTOS", primeiroDiaMes, ultimoDiaMes);
        despesaEquipamentos = despesaEquipamentos == null ? BigDecimal.ZERO : despesaEquipamentos;

        BigDecimal despesaAluguel = despesaRepository.sumValorByCategoriaAndPeriodo("ALUGUEL", primeiroDiaMes, ultimoDiaMes);
        despesaAluguel = despesaAluguel == null ? BigDecimal.ZERO : despesaAluguel;

        // Adicionando a Despesa LUZ que está no V5 (popula_db_test.sql) e deve ser contabilizada.
        BigDecimal despesaLuz = despesaRepository.sumValorByCategoriaAndPeriodo("LUZ", primeiroDiaMes, ultimoDiaMes);
        despesaLuz = despesaLuz == null ? BigDecimal.ZERO : despesaLuz;

        BigDecimal despesaTotal = despesaSalarios
                .add(despesaEquipamentos)
                .add(despesaAluguel)
                .add(despesaLuz);

        FinanceiroDashboardDTO dto = new FinanceiroDashboardDTO();

        dto.setReceitaTotal(receitaTotal);
        dto.setDespesaTotal(despesaTotal);
        dto.setLucroLiquido(receitaTotal.subtract(despesaTotal));

        Map<String,BigDecimal> receitaPorFonte = new HashMap<>();
        receitaPorFonte.put("Mensalidades", receitaMensalidades);
        receitaPorFonte.put("Produto", receitaProduto);
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
        if (despesaLuz.compareTo(BigDecimal.ZERO) > 0) {
            despesaPorCategoria.put("Luz", despesaLuz);
        }
        dto.setDespesaPorCategoria(despesaPorCategoria);

        return dto;
    }
}