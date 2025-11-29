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
import com.devfitcorp.devfit.service.AuthService;
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

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastroAluno(@RequestBody CadastroRequest request) {
        authService.registrarAluno(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Aluno registrado com sucesso!");
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping("/cadastro/instrutor")
    public ResponseEntity<String> cadastroInstrutor(@RequestBody CadastroRequest request) {
        authService.registrarInstrutor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Instrutor registrado com sucesso.");
    }

    @PostMapping("/cadastro/gestor")
    public ResponseEntity<String> cadastroGestor(@RequestBody CadastroGestorRequest request) {
        authService.registrarGestor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Gestor registrado com sucesso.");
    }
}
