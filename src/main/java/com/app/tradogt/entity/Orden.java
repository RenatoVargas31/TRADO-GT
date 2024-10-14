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

    @Size(max = 45)
    @NotNull
    @Column(name = "codigo", nullable = false, length = 45)
    private String codigo;

    @NotNull
    @Column(name = "costoTotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoTotal;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pago_idPago", nullable = false)
    private Pago pagoIdpago;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Carrito_idCarrito", nullable = false)
    private Carrito carritoIdcarrito;

    @OneToOne
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuarioIdusuario;

    @NotNull
    @Column(name = "LugarEntrega")
    private String lugarEntrega;

    @Column(name = "fechaValidacion", nullable = false)
    private LocalDate fechaValidacion;

    @Column(name = "fechaEnProceso", nullable = false)
    private LocalDate fechaEnProceso;

    @NotNull
    @Column(name = "fechaEnAduanas", nullable = false)
    private LocalDate fechaEnAduanas;

    @NotNull
    @Column(name = "fechaEnRuta", nullable = false)
    private LocalDate fechaEnRuta;

    @NotNull
    @Column(name = "fechaRecibido", nullable = false)
    private LocalDate fechaRecibido;

}
