package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "matriculas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter // tirei o @Data pois podia dar loop infinito nos logs, por conta do @ToString embutido
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    private Plano plano;

    @OneToOne(mappedBy = "matricula") // relacionamento bidirecional
    private Usuario usuario;

    private LocalDate dataVencimento;

    private boolean isAtiva;
}