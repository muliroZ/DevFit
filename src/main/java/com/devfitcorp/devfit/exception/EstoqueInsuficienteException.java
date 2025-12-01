package com.devfitcorp.devfit.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(Long id, String nome) {
        super("Não há estoque suficiente do produto " +nome + " com id " +id );
    }
}
