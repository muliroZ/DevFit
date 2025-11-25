package com.devfitcorp.devfit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)

public class ResourceNotFoundException extends RuntimeException {

    // Construtor que aceita o ID da entidade não encontrada

    public ResourceNotFoundException(Long id) {
        super("Recurso (ID " + id + ") não encontrado.");
    }

    // Construtor para mensagens mais específicas

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
