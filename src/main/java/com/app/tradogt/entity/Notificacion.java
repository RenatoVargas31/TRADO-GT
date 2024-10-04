package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "Notificacion")
public class Notificacion {
    @Id
    @Column(name = "idNotificacion", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @Size(max = 45)
    @NotNull
    @Column(name = "contenido", nullable = false, length = 45)
    private String contenido;

}
