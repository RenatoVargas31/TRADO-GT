package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "Pregunta")
public class Pregunta {
    @Id
    @Column(name = "idPregunta", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @Size(max = 80)
    @NotNull
    @Column(name = "contenido", nullable = false, length = 80)
    private String contenido;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

}
