package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

import java.time.Instant;

@Data
@Entity
@Table(name = "UsuarioRespuesta")
public class UsuarioRespuesta {
    @EmbeddedId
    private UsuarioRespuestaId id;

    @MapsId("preguntaIdpregunta")
    @ManyToOne
    @JoinColumn(name = "Pregunta_idPregunta", nullable = false)
    private Pregunta preguntaIdpregunta;

    @MapsId("usuarioIdusuario")
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @Size(max = 100)
    @NotNull
    @Column(name = "Contenido", nullable = false, length = 100)
    private String contenido;

    @NotNull
    @Column(name = "Fecha", nullable = false)
    private Instant fecha;

}
