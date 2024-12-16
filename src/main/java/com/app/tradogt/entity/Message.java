package com.app.tradogt.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Chat")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idChat", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "mensaje", nullable = false, length = 45)
    private String mensaje;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_idUsuario", nullable = false)
    private Usuario sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "orden_idOrden", nullable = false)
    private Orden orden;

    @Column(name = "tiempo")
    private LocalDateTime fechaEnvio;

}