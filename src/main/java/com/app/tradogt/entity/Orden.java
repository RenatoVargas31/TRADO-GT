package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Orden")
public class Orden {
    @Id
    @Column(name = "idOrden", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @ManyToOne
    @JoinColumn(name = "agentCompra_idUsuario")
    private Usuario agentcompraIdusuario;

    @ManyToOne
    @JoinColumn(name = "estadoOrden_idEstadoOrden")
    private EstadoOrden estadoordenIdestadoorden;

    @Column(name = "fechaCreacion")
    private Instant fechaCreacion;

    @Column(name = "fechaArribo")
    private LocalDate fechaArribo;

    @Column(name = "valoracionAgente")
    private Byte valoracionAgente;

    @ColumnDefault("0")
    @Column(name = "isDeleted")
    private Byte isDeleted;

    @Column(name = "fueRapido")
    private Byte fueRapido;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "esCarrito", nullable = false)
    private Byte esCarrito = 1;

}
