package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "codigodespachador")
public class CodigoDespachador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCodigoDespachador", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "Caracteres", nullable = false, length = 45)
    private String caracteres;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Distrito_idDistrito", nullable = false)
    private Distrito distritoIddistrito;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "EstadoCodigo_idEstadoCodigo", nullable = false)
    private EstadoCodigo estadocodigoIdestadocodigo;

}
