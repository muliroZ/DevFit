package com.devfitcorp.devfit.dto;

public interface CadastroBase {
    String nome();
    String senha();
    String email();

    default void validarCodigo(String codigo) {}
}
