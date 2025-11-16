package com.devfitcorp.devfit.controller;

import com.devfitcorp.devfit.dto.AuthResponse;
import com.devfitcorp.devfit.dto.CadastroGestorRequest;
import com.devfitcorp.devfit.dto.CadastroRequest;
import com.devfitcorp.devfit.dto.LoginRequest;
import com.devfitcorp.devfit.model.Role;
import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.repository.RoleRepository;
import com.devfitcorp.devfit.repository.UsuarioRepository;
import com.devfitcorp.devfit.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${ADMIN_SECRET}")
    private String admSecret;

    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthenticationManager authManager,
            PasswordEncoder passwordEncoder,
            UsuarioRepository usuarioRepository,
            RoleRepository roleRepository,
            JwtUtil jwtUtil
    ) {
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        UserDetails usuario = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(usuario.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastroAluno(@RequestBody CadastroRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O usuário já existe.");
        }

        Role defaultRole = roleRepository.findByNome("ROLE_ALUNO")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ALUNO")));

        Usuario aluno = new Usuario();
        aluno.setNome(request.nome());
        aluno.setSenha(passwordEncoder.encode(request.senha()));
        aluno.setEmail(request.email());
        aluno.setRoles(Set.of(defaultRole));

        usuarioRepository.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário (Aluno) registrado com sucesso.");
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping("/cadastro/instrutor")
    public ResponseEntity<String> cadastroInstrutor(@RequestBody CadastroRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Instrutor já registrado.");
        }

        Role instrutorRole = roleRepository.findByNome("ROLE_INSTRUTOR")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_INSTRUTOR")));

        Usuario instrutor = new Usuario();
        instrutor.setNome(request.nome());
        instrutor.setSenha(passwordEncoder.encode(request.senha()));
        instrutor.setEmail(request.email());
        instrutor.setRoles(Set.of(instrutorRole));

        usuarioRepository.save(instrutor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Instrutor registrado com sucesso.");
    }

    @PostMapping("/cadastro/gestor")
    public ResponseEntity<String> cadastroGestor(@RequestBody CadastroGestorRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já registrado.");
        }

        if (!request.gestorCode().equals(admSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Código de verificação inválido");
        }

        Role gestorRole = roleRepository.findByNome("ROLE_GESTOR")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_GESTOR")));

        Usuario gestor = new Usuario();
        gestor.setNome(request.nome());
        gestor.setSenha(passwordEncoder.encode(request.senha()));
        gestor.setEmail(request.email());
        gestor.setRoles(Set.of(gestorRole));

        usuarioRepository.save(gestor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Gestor registrado com sucesso.");
    }
}
