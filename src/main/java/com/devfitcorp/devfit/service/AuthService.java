package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.*;
import com.devfitcorp.devfit.exception.UsuariojaExisteException;
import com.devfitcorp.devfit.mappers.AuthMapper;
import com.devfitcorp.devfit.model.Role;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.model.UsuarioRole;
import com.devfitcorp.devfit.repository.RoleRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import com.devfitcorp.devfit.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    @Value("${ADMIN_SECRET}")
    private String admSecret;

    private final AuthMapper authMapper;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(
            AuthMapper authMapper,
            UsuarioRepository usuarioRepository,
            RoleRepository roleRepository,
            AuthenticationManager authManager,
            JwtUtil jwtUtil
    ) {
        this.authMapper = authMapper;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        Usuario usuario = (Usuario) auth.getPrincipal();
        String rolePrincipal = determinarRolePrincipal(usuario);
        String token = jwtUtil.generateToken(usuario.getEmail(), rolePrincipal, usuario.getId(), usuario.getNome());

        return new AuthResponse(token);
    }

    public void registrarGestor(CadastroGestorRequest request) {
        request.validarCodigo(admSecret);
        registrarComRole(request, UsuarioRole.GESTOR);
    }

    public void registrarAluno(CadastroRequest request) {
        registrarComRole(request, UsuarioRole.ALUNO);
    }

    public void registrarInstrutor(CadastroRequest request) {
        registrarComRole(request, UsuarioRole.INSTRUTOR);
    }

    private void registrarComRole(CadastroBase request, UsuarioRole usuarioRole) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new UsuariojaExisteException("O usuário já existe");
        }

        Role role = roleRepository.findByNome(usuarioRole)
                .orElseThrow(() -> new RuntimeException("Role não encontrada: " + usuarioRole));

        usuarioRepository.save(authMapper.toEntity(request, role));
    }

    private String determinarRolePrincipal(UserDetails usuario) {

        // Mapeia todas as authorities para uma lista de Strings (ex: ["ROLE_GESTOR", "ROLE_ALUNO"])
        List<String> authorities = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (authorities.contains("ROLE_GESTOR")) {
            return "ROLE_GESTOR";
        }
        if (authorities.contains("ROLE_INSTRUTOR")) {
            return "ROLE_INSTRUTOR";
        }
        if (authorities.contains("ROLE_ALUNO")) {
            return "ROLE_ALUNO";
        }

        // Caso de fallback (se a autenticação passou, mas nenhuma role foi encontrada)
        throw new RuntimeException("Usuário autenticado sem role definida no sistema.");
    }
}
