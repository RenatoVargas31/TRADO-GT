package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estadoordenagente")
public class EstadoOrdenAgente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoOrdenAgente", nullable = false)
    private Integer idEstadoOrdenAgente;

    @Size(max = 45)
    @NotNull
    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

}