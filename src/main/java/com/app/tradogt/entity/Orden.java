package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Orden")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrden", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "agentCompra_idUsuario")
    private Usuario agentcompraIdusuario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "estadoOrden_idEstadoOrden", nullable = false)
    private EstadoOrden estadoordenIdestadoorden;

    @NotNull
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @Column(name = "fechaArribo", nullable = false)
    private LocalDate fechaArribo;

    @Column(name = "valoracionAgente")
    private Byte valoracionAgente;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isDeleted", nullable = false)
    private Byte isDeleted;

    @NotNull
    @Column(name = "fueRapido", nullable = false)
    private Byte fueRapido;

    @Size(max = 45)
    @NotNull
    @Column(name = "codigo", nullable = false, length = 45)
    private String codigo;

    @Column(name = "subTotal")
    private BigDecimal subTotal;

    @NotNull
    @Column(name = "costoTotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoTotal;
}
