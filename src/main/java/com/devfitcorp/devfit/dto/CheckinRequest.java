package com.devfitcorp.devfit.dto;

import com.devfitcorp.devfit.model.CheckinType;
import jakarta.validation.constraints.NotNull;

public record CheckinRequest (
        @NotNull(message = "O ID do usuário não pode ser nulo.") Long usuarioId;
        CheckinType tipo
) {
}
