package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern; // Import necessário
import jakarta.validation.constraints.Size;

public record CadastroGestorRequest(
        @NotBlank(message = "O nome de usuário é obrigatório")
        String nome,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número."
        )
        String senha,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "Código de administrador obrigatório")
        String gestorCode
) implements CadastroBase {

    @Override
    public void validarCodigo(String codigo) {
        if (!gestorCode.equals(codigo)) {
            throw new RuntimeException();
        }
    }
}