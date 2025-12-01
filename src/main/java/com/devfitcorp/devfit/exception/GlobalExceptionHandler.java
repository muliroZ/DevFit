package com.devfitcorp.devfit.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        String cause = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage().toLowerCase()
                : "";

        // Caso 1 — Violação de UNIQUE (registro duplicado)
        if (cause.contains("unique") || cause.contains("duplicate")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Já existe um registro com esses dados. Verifique e tente novamente.");
        }

        // Caso 2 — Violação de Foreign Key (FK)
        if (cause.contains("foreign key")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Este registro está sendo usado em outra parte do sistema e não pode ser removido.");
        }

        // Caso genérico
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Operação não permitida: os dados violam regras do banco.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Acesso negado. Você não possui permissão para realizar esta operação.");
    }



    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<?> handleProdutoNaoEncontradoException(ProdutoNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<?> handleProdutoInsuficienteException(EstoqueInsuficienteException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        var erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros);
    }



    @ExceptionHandler(UsuariojaExisteException.class)
    public ResponseEntity<?> handleUsuarioJaExisteException(UsuariojaExisteException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    // Novo Handler, captura erros de logica de negocio e valicadao na camada de service
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<?> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage() != null ? ex.getMessage() : "Ocorreu um erro interno inesperado");
    }
}
