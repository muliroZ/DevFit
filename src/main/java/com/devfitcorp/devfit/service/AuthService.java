package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.dto.*;
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

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(
            UsuarioRepository usuarioRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            JwtUtil jwtUtil
    ) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        String token = jwtUtil.generateToken(usuario.getUsername());

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
            throw new RuntimeException(); // placeholder
        }

        Role role = roleRepository.findByNome(usuarioRole)
                .orElseThrow(() -> new RuntimeException("Role não encontrada: " + usuarioRole));

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setEmail(request.email());
        usuario.setRoles(Set.of(role));

        usuarioRepository.save(usuario);
    }
}
