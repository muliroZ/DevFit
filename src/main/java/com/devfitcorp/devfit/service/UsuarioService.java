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

    public Long getUserIdFromUsername(String username) {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException(("Usuário logado não encontrado no banco de dados.")));

        return usuario.getId();
    }

    public Map<String, List<UsuarioDetalhadoDashboardDTO>> findAndGroupByRole() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(UsuarioDetalhadoDashboardDTO::fromEntity)
                .collect(Collectors.groupingBy(
                        UsuarioDetalhadoDashboardDTO::rolePrincipal
                ));
    }
}