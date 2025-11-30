package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.AdminStatsDTO;
import com.devfitcorp.devfit.repository.EquipamentoRepository;
import com.devfitcorp.devfit.repository.MatriculaRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import com.devfitcorp.devfit.service.FinanceiroService;
import com.devfitcorp.devfit.dto.CheckinStatsByHourDTO;
import java.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public AdminDashboardService(
            UsuarioRepository usuarioRepository,
            MatriculaRepository matriculaRepository,
            EquipamentoRepository equipamentoRepository,
            FinanceiroService financeiroService,
            CheckinService checkinService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.matriculaRepository = matriculaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.financeiroService = financeiroService;
        this.checkinService = checkinService;
    }

    public AdminStatsDTO getGeneralStats() {
        AdminStatsDTO dto = new AdminStatsDTO();


        dto.setFaturamentoMensalPrevisto(BigDecimal.valueOf(15000.00));

        long ativos = matriculaRepository.countByIsAtiva(true);
        long inativos = matriculaRepository.countByIsAtiva(false);
        long totalAlunos = ativos + inativos;

        dto.setTotalAlunosAtivos(ativos);
        dto.setTotalAlunosInativos(inativos);

        if (totalAlunos > 0) {
            double taxa = (double) ativos / totalAlunos;
            dto.setTaxaRetencao(BigDecimal.valueOf(taxa).setScale(4, RoundingMode.HALF_UP).doubleValue());
        } else {
            dto.setTaxaRetencao(0.0);
        }

        dto.setTotalUsuariosCadastrados(usuarioRepository.count());
        dto.setCapacidadeMaxima(CAPACIDADE_MAXIMA);

        long manutencao = equipamentoRepository.countByStatus("MANUTENCAO");
        long totais = equipamentoRepository.count();

        dto.setEquipamentosEmManutencao(manutencao);
        dto.setEquipamentosTotais(totais);

        dto.setCheckinsHoje(checkinService.countCheckinsToday());

        return dto;
    }

    //Método para obter estatísticas de Horários de Pico (Gráfico)
    public List<CheckinStatsByHourDTO> getPeakHoursStats(LocalDate data) {
        return checkinService.getPeakHoursStats(data);
    }
}