package com.devfitcorp.devfit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckinStatsByHourDTO {
    private int hora;
    private Long contagem;
}