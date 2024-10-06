package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Comentario")
public class Comentario {
    @Id
    @Column(name = "idComentario", nullable = false)
    private Integer id;

    @NotNull
    @Lob
    @Column(name = "cuerpo", nullable = false)
    private String cuerpo;

    @NotNull
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Publicacion_idPublicacion", nullable = false)
    private Publicacion publicacionIdpublicacion;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

}
