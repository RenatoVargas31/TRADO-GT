package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Pago")
public class Pago {
    @Id
    @Column(name = "idPago", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "orden_idOrden", nullable = false)
    private Orden ordenIdorden;

    @Size(max = 45)
    @NotNull
    @Column(name = "metodo", nullable = false, length = 45)
    private String metodo;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "fechaTarjeta")
    private String fechaTarjeta;

    @Column(name = "numeroTarjeta")
    private String numeroTarjeta;

    @Column(name = "nombreTarjeta")
    private String nombreTarjeta;

    @Column(name = "codigoCVV")
    private String codigoCVV;



}