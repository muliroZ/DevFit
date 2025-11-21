package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.EquipamentoDashboardDTO;
import com.devfitcorp.devfit.repository.EquipamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class EquipamentoService {
    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<EquipamentoDashboardDTO> listarTodosParaDashboard() {
        return equipamentoRepository.findAll().stream()
                .map(EquipamentoDashboardDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
