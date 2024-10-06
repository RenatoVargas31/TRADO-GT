package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Publicacion")
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPublicacion", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "titulo", nullable = false, length = 45)
    private String titulo;

    @NotNull
    @Lob
    @Column(name = "cuerpo", nullable = false)
    private String cuerpo;

    @NotNull
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

}
