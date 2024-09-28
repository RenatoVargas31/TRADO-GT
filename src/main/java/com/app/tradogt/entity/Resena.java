package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "resena")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idResena", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "Foto", nullable = false, length = 45)
    private String foto;

    @Size(max = 200)
    @NotNull
    @Column(name = "Comentario", nullable = false, length = 200)
    private String comentario;

    @Size(max = 45)
    @NotNull
    @Column(name = "Calidad", nullable = false, length = 45)
    private String calidad;

    @Size(max = 45)
    @NotNull
    @Column(name = "TiempoEntrega", nullable = false, length = 45)
    private String tiempoEntrega;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Producto_idProducto", nullable = false)
    private Producto productoIdproducto;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

}
