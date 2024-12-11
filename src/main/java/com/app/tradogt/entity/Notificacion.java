package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Notificacion")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNoti", nullable = false)
    private Integer idNoti;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idUsuarioNoti", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idOrdenNoti", nullable = false)
    private Orden orden;

    @NotNull
    @Column(name = "mensaje", nullable = false)
    private String contenido;

    @Column(name = "leida")
    private Boolean leido;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

}
