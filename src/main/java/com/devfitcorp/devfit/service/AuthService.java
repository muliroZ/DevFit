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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        UserDetails usuario = (UserDetails) auth.getPrincipal();
        String role = usuario.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.generateToken(usuario.getUsername(), role);

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
}
