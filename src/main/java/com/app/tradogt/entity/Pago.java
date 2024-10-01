package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPago", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Orden_idOrden", nullable = false)
    private Orden orden;

    @Size(max = 45)
    @NotNull
    @Column(name = "Metodo", nullable = false, length = 45)
    private String metodo;

    @NotNull
    @Column(name = "Monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotNull
    @Column(name = "Fecha", nullable = false)
    private Instant fecha;

}