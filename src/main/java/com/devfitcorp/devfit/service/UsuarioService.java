package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.UsuarioDetalhadoDashboardDTO;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca todos os usuários e os agrupa por seu primeiro Cargo (Role) para o Dashboard.
     */
    public Map<String, List<UsuarioDetalhadoDashboardDTO>> findAndGroupByRole() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Processamento dos dados: Stream -> Mapeamento para DTO -> Agrupamento
        return usuarios.stream()
                // 1. Mapeia a Entidade (Usuario) para o DTO Detalhado
                .map(UsuarioDetalhadoDashboardDTO::fromEntity)
                // 2. Agrupa por Cargo (Role)
                .collect(Collectors.groupingBy(
                        // Usa o primeiro Cargo do Set como chave de agrupamento
                        dto -> dto.roles().iterator().next()
                ));
    }
}