package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "Distrito")
public class Distrito implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDistrito", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "zona_idZona", nullable = false)
    private Zona zonaIdzona;

    @Size(max = 45)
    @NotNull
    @Column(name = "codigo", nullable = false, length = 45)
    private String codigo;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

}
