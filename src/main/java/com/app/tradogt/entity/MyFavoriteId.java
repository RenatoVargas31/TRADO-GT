package com.app.tradogt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MyFavoriteId implements java.io.Serializable {

    @NotNull
    @Column(name = "usuario_idUsuario", nullable = false)
    private Integer usuarioIdusuario;

    @NotNull
    @Column(name = "productoEnZona_id", nullable = false)
    private Integer productoenzonaId;

}