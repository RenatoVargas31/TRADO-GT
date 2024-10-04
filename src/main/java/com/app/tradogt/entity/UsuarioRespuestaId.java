package com.app.tradogt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UsuarioRespuestaId implements Serializable {

    @NotNull
    @Column(name = "Pregunta_idPregunta", nullable = false)
    private Integer preguntaIdpregunta;

    @NotNull
    @Column(name = "Usuario_idUsuario", nullable = false)
    private Integer usuarioIdusuario;
}