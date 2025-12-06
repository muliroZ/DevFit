package com.devfitcorp.devfit.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devfitcorp.devfit.dto.AdminStatsDTO;
import com.devfitcorp.devfit.dto.CheckinStatsByHourDTO;
import com.devfitcorp.devfit.dto.FinanceiroDashboardDTO;
import com.devfitcorp.devfit.mappers.DashboardMapper;
import com.devfitcorp.devfit.repository.EquipamentoRepository;
import com.devfitcorp.devfit.repository.MatriculaRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;

@Service
public class AdminDashboardService {

    //Capacidade fixa
    private static final int CAPACIDADE_MAXIMA = 500;

    private final UsuarioRepository usuarioRepository;
    private final MatriculaRepository matriculaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final FinanceiroService financeiroService;
    private final CheckinService checkinService;
    private final DashboardMapper dashboardMapper;

    public AdminDashboardService(
            UsuarioRepository usuarioRepository,
            MatriculaRepository matriculaRepository,
            EquipamentoRepository equipamentoRepository,
            FinanceiroService financeiroService,
            CheckinService checkinService,
            DashboardMapper dashboardMapper
    ) {
        this.usuarioRepository = usuarioRepository;
        this.matriculaRepository = matriculaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.financeiroService = financeiroService;
        this.checkinService = checkinService;
        this.dashboardMapper = dashboardMapper;
    }

    public AdminStatsDTO getGeneralStats() {
        try {
            HashMap<String, Object> stats = new HashMap<>();

            FinanceiroDashboardDTO financeiro = financeiroService.getAggregatedFinanceiroSummary();

            // Extração das estatísticas (os metodos q tu colocou foram reutilizados marolina)
            Long ativos = matriculaRepository.countByIsAtiva(true);
            Long inativos = matriculaRepository.countByIsAtiva(false);
            long totalAlunos = ativos + inativos;
            Double taxa = totalAlunos > 0 ? (double) ativos / totalAlunos : 0;
            Long manutencao = equipamentoRepository.countByStatus("MANUTENCAO");
            Long totais = equipamentoRepository.count();

            // Coloquei num HashMap (dicionário) pra usar o mapper lá no final (fica organizado)
            stats.put("faturamentoMensalPrevisto", BigDecimal.valueOf(15000.00));
            stats.put("totalAlunosOn", ativos);
            stats.put("totalAlunosOff", inativos);
            stats.put("taxaRetencao", taxa);
            stats.put("totalAlunos", usuarioRepository.count());
            stats.put("capacidadeMax", CAPACIDADE_MAXIMA);
            stats.put("manutencaoEquip", manutencao);
            stats.put("totalEquip", totais);
            stats.put("checkinsHoje", checkinService.countCheckinsToday());
            stats.put("financeiro", financeiro);

            // Mapper pra organizar (veja o DashboardMapper)
            return dashboardMapper.toResponse(stats);

        } catch (Exception e) {
            throw new RuntimeException("Erro: ", e);
        }
    }

    //Método para obter estatísticas de Horários de Pico (Gráfico)
    public List<CheckinStatsByHourDTO> getPeakHoursStats(LocalDate data) {
        return checkinService.getPeakHoursStats(data);
    }
}