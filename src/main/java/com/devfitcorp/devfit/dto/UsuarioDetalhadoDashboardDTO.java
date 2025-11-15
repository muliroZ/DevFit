package com.devfitcorp.devfit.dto;

import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.model.Matricula;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;


public record UsuarioDetalhadoDashboardDTO(
        Long id,
        String nome,
        String email,
        Set<String> roles,


        String planoNome,
        BigDecimal valorMensalidade,
        LocalDate dataVencimentoMensalidade,


        Integer diasFaltososNoMes
) {
    public static UsuarioDetalhadoDashboardDTO fromEntity(Usuario usuario) {

        Set<String> roleNomes = usuario.getRoles().stream()
                .map(role -> role.getNome())
                .collect(Collectors.toSet());


        Matricula matricula = usuario.getMatricula();


        String planoNome = null;
        BigDecimal valorMensalidade = null;
        LocalDate dataVencimento = null;

        if (matricula != null) {

            dataVencimento = matricula.getDataVencimento();

            if (matricula.getPlano() != null) {
                planoNome = matricula.getPlano().getNome();
                valorMensalidade = matricula.getPlano().getValor();
            }
        }

        return new UsuarioDetalhadoDashboardDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                roleNomes,


                planoNome,
                valorMensalidade,
                dataVencimento,
                usuario.getDiasFaltososNoMes()
        );
    }
}