package com.devfitcorp.devfit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckinRequest {

    @NotNull(message = "O ID do usuário não pode ser nulo.")
    private Long usuarioId;
    private String tipo;


}