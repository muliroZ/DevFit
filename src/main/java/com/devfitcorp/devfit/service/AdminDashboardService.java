package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.AdminStatsDTO;
import com.devfitcorp.devfit.mappers.DashboardMapper;
import com.devfitcorp.devfit.repository.EquipamentoRepository;
import com.devfitcorp.devfit.repository.MatriculaRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import com.devfitcorp.devfit.dto.CheckinStatsByHourDTO;
import java.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

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
        HashMap<String, Object> stats = new HashMap<>();

        financeiroService.getAggregatedFinanceiroSummary();

        // Extração das estatísticas (os metodos q tu colocou foram reutilizados marolina)
        long ativos = matriculaRepository.countByIsAtiva(true);
        long inativos = matriculaRepository.countByIsAtiva(false);
        long totalAlunos = ativos + inativos;
        double taxa = totalAlunos > 0 ? (double) ativos / totalAlunos : 0;
        long manutencao = equipamentoRepository.countByStatus("MANUTENCAO");
        long totais = equipamentoRepository.count();

        // Coloquei num HashMap (dicionário) pra usar o mapper lá no final (fica organizado)
        stats.put("faturamentoMP", BigDecimal.valueOf(15000.00));
        stats.put("totalAlunosOn", ativos);
        stats.put("totalAlunosOff", inativos);
        stats.put("taxaRetencao", BigDecimal.valueOf(taxa).setScale(4, RoundingMode.HALF_UP).doubleValue());
        stats.put("totalUsers", usuarioRepository.count());
        stats.put("capacidadeMax", CAPACIDADE_MAXIMA);
        stats.put("mautencaoEquip", manutencao);
        stats.put("totalEquip", totais);
        stats.put("checkinsHoje", checkinService.countCheckinsToday());
        stats.put("financeiro", financeiroService.getAggregatedFinanceiroSummary());

        // Mapper pra organizar (veja o DashboardMapper)
        return dashboardMapper.toResponse(stats);
    }

    //Método para obter estatísticas de Horários de Pico (Gráfico)
    public List<CheckinStatsByHourDTO> getPeakHoursStats(LocalDate data) {
        return checkinService.getPeakHoursStats(data);
    }
}