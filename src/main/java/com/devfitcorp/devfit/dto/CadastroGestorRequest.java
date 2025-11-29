package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroGestorRequest(
        @NotBlank(message = "O nome de usuário é obrigatório") String nome,
        @NotBlank(message = "A senha é obrigatória") @Size(min = 8) String senha,
        @NotBlank(message = "O email é obrigatório") @Email(message = "Formato de email inválido") String email,
        @NotBlank(message = "Código de administrador obrigatório") String gestorCode
) implements CadastroBase {

    @Override
    public void validarCodigo(String codigo) {
        if (!gestorCode.equals(codigo)) {
            throw new RuntimeException(); // placeholder
        }
    }
}
