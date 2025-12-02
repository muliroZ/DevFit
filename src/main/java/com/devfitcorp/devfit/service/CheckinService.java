package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.CheckinStatsByHourDTO;
import com.devfitcorp.devfit.exception.UsuarioNaoEncontradoException;
import com.devfitcorp.devfit.model.Checkin;
import com.devfitcorp.devfit.model.CheckinType; // ENTRADA ou SAIDA
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.CheckinRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import com.devfitcorp.devfit.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final UsuarioRepository usuarioRepository;
    private final MatriculaRepository matriculaRepository;

    public CheckinService(
            CheckinRepository checkinRepository,
            UsuarioRepository usuarioRepository,
            MatriculaRepository matriculaRepository
    ) {
        this.checkinRepository = checkinRepository;
        this.usuarioRepository = usuarioRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public Checkin registrarCheckin(Long usuarioId, CheckinType tipo) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        boolean matriculaAtiva = matriculaRepository.existsByUsuarioIdAndIsAtivaTrue(usuarioId);

        if (!matriculaAtiva) {
            throw new RuntimeException("Matrícula vencida ou inativa. Acesso negado.");
        }

        Checkin checkin = new Checkin();
        checkin.setUsuario(usuario);
        checkin.setTipo(tipo);
        checkin.setDataHora(LocalDateTime.now());

        return checkinRepository.save(checkin);
    }

    //Conta os checks-in no dia atual
    public long countCheckinsToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return checkinRepository.countByDataHoraBetween(startOfDay, endOfDay);
    }

    //Gráfico para o Dashboard
    public List<CheckinStatsByHourDTO> getPeakHoursStats(LocalDate data) {

        return checkinRepository.findCheckinCountByHour(data).stream()
                .map(result -> new CheckinStatsByHourDTO((Integer) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }
}