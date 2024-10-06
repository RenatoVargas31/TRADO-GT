package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Resena")
public class Resena {
    @Id
    @Column(name = "idResena", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Column(name = "foto", nullable = false, length = 150)
    private String foto;

    @NotNull
    @Lob
    @Column(name = "cuerpo", nullable = false)
    private String cuerpo;

    @NotNull
    @Column(name = "calidad", nullable = false)
    private Integer calidad;

    @NotNull
    @Column(name = "calificacion", nullable = false)
    private Integer calificacion;

    @Size(max = 150)
    @NotNull
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @NotNull
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @ManyToOne
    @MapsId("carritoId")
    @JoinColumns({
            @JoinColumn(name = "Carrito_ProductoEnZona_producto_idProducto", referencedColumnName = "ProductoEnZona_producto_idProducto", nullable = false),
            @JoinColumn(name = "Carrito_ProductoEnZona_zona_idZona", referencedColumnName = "ProductoEnZona_zona_idZona", nullable = false),
            @JoinColumn(name = "Carrito_Usuario_idUsuario", referencedColumnName = "Usuario_idUsuario", nullable = false)
    })
    private Carrito carrito;

}
