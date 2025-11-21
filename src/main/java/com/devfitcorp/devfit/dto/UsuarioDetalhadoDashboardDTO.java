package com.devfitcorp.devfit.dto;

import com.devfitcorp.devfit.model.Usuario;
import com.devfitcorp.devfit.model.Matricula;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;


public record UsuarioDetalhadoDashboardDTO(
        Long id,
        String nome,
        String email,
        Set<String> roles,
        String rolePrincipal,


        String planoNome,
        BigDecimal valorMensalidade,
        LocalDate dataVencimentoMensalidade,


        Integer diasFaltososNoMes
) {

    private static final List<String> ORDEM_PRIORIDADE = Arrays.asList(
            "GESTOR",
            "INSTRUTOR",
            "ALUNO"
    );

    private static String definirRolePrincipal(Set<String> roleNomes) {
        for (String rolePrioritaria : ORDEM_PRIORIDADE) {
            if (roleNomes.contains(rolePrioritaria)) {
                return rolePrioritaria;
            }
        }
        if (!roleNomes.isEmpty()) {
            return roleNomes.iterator().next();
        }
        return "SEM_ROLE";
    }


    public static UsuarioDetalhadoDashboardDTO fromEntity(Usuario usuario) {

        Set<String> roleNomes = usuario.getRoles().stream()
                .map(role -> role.getNome().toString())
                .collect(Collectors.toSet());

        String rolePrincipal = definirRolePrincipal(roleNomes);

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
                rolePrincipal,
                planoNome,
                valorMensalidade,
                dataVencimento,
                usuario.getDiasFaltososNoMes()
        );
    }
}