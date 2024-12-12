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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idResena", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Producto_idProducto", nullable = false)
    private Producto productoIdproducto;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @Lob
    @Basic(fetch = FetchType.LAZY) // Opcional: si prefieres cargarlo bajo demanda
    @Column(name = "foto")
    private byte[] foto;


    @NotNull
    @Lob
    @Column(name = "cuerpo", nullable = false)
    private String cuerpo;

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
    @Column(name = "fueRapido", nullable = false)
    private Byte fueRapido;

}
