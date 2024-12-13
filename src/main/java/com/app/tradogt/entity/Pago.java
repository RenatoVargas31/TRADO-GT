package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @Pattern(regexp="\\d+", message="Debe contener solo números")
    @Size(min=16, max = 16, message = "Debe tener 16 dígitos")
    @NotNull(message = "Debe ingresar su numero de tarjeta")
    @Positive(message = "El número debe ser mayor a cero")
    @Column(name = "numeroTarjeta")
    private String numeroTarjeta;

    
    @Column(name = "nombreTarjeta")
    private String nombreTarjeta;

    @NotNull(message = "Debe colocar el código CVV")
    @Size(min=3, max = 4, message = "Debe tener 3 dígitos")
    @Pattern(regexp="\\d+", message="Debe contener solo números")
    @Column(name = "codigoCVV")
    private String codigoCVV;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Autenticacion_idAutenticacion", nullable = false)
    private Autenticacion autenticacionIdautenticacion;

}