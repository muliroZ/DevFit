package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.EquipamentoDashboardDTO;
import com.devfitcorp.devfit.dto.EquipamentoRequest;
import com.devfitcorp.devfit.mappers.EquipamentoMapper;
import com.devfitcorp.devfit.model.Equipamento;
import com.devfitcorp.devfit.repository.EquipamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class EquipamentoService {
    private final EquipamentoRepository equipamentoRepository;
    private final EquipamentoMapper equipamentoMapper;

    public EquipamentoService(
            EquipamentoRepository equipamentoRepository,
            EquipamentoMapper equipamentoMapper
    ) {
        this.equipamentoRepository = equipamentoRepository;
        this.equipamentoMapper = equipamentoMapper;
    }

    public List<EquipamentoDashboardDTO> listarTodosParaDashboard() {
        return equipamentoRepository.findAll().stream()
                .map(EquipamentoDashboardDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Equipamento salvar(EquipamentoRequest request) {
        return equipamentoRepository.save(equipamentoMapper.toEntity(request));
    }
}
