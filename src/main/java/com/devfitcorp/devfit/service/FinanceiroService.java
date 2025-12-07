package com.devfitcorp.devfit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;
import com.devfitcorp.devfit.mappers.FinanceiroMapper;
import com.devfitcorp.devfit.repository.DespesaRepository;
import com.devfitcorp.devfit.repository.MensalidadeRepository;
import com.devfitcorp.devfit.repository.PedidoRepository;

@Service
public class FinanceiroService {

    private final MensalidadeRepository mensalidadeRepository;
    private final DespesaRepository despesaRepository;
    private final PedidoRepository pedidoRepository;
    private final FinanceiroMapper financeiroMapper;

    public FinanceiroService(
            MensalidadeRepository mensalidadeRepository,
            DespesaRepository despesaRepository,
            PedidoRepository pedidoRepository,
            FinanceiroMapper financeiroMapper
    ){
        this.mensalidadeRepository = mensalidadeRepository;
        this.despesaRepository = despesaRepository;
        this.pedidoRepository = pedidoRepository;
        this.financeiroMapper = financeiroMapper;
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

        BigDecimal despesaLuz = despesaRepository.sumValorByCategoriaAndPeriodo("LUZ", primeiroDiaMes, ultimoDiaMes);
        despesaLuz = despesaLuz == null ? BigDecimal.ZERO : despesaLuz;

        BigDecimal despesaTotal = despesaSalarios
                .add(despesaEquipamentos)
                .add(despesaAluguel)
                .add(despesaLuz);

        Map<String,BigDecimal> receitaPorFonte = new HashMap<>();
        receitaPorFonte.put("Mensalidades", receitaMensalidades);
        receitaPorFonte.put("Produto", receitaProduto);

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

        return financeiroMapper.toResponse(
                receitaTotal,
                despesaTotal,
                receitaTotal.subtract(despesaTotal),
                (HashMap<String, BigDecimal>) receitaPorFonte,
                (HashMap<String, BigDecimal>) despesaPorCategoria
        );
    }
}