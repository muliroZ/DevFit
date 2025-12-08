package com.devfitcorp.devfit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "checkins")
@Getter
@Setter
@NoArgsConstructor
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING) //marca a entrada e saida dos alunos
    private CheckinType tipo;

    @PrePersist
    public void onCreate() {
        this.dataHora = LocalDateTime.now();
    }
}
