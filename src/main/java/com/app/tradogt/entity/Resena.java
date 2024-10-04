package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "Resena")
public class Resena {
    @Id
    @Column(name = "idResena", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Producto_idProducto", nullable = false)
    private Producto productoIdproducto;

    @Size(max = 100)
    @NotNull
    @Column(name = "foto", nullable = false, length = 100)
    private String foto;

    @Size(max = 250)
    @NotNull
    @Column(name = "comentario", nullable = false, length = 250)
    private String comentario;

    @NotNull
    @Column(name = "calidad", nullable = false)
    private Byte calidad;

    @NotNull
    @Column(name = "calificacion", nullable = false)
    private Byte calificacion;

}
