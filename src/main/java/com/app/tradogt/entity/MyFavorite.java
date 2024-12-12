package com.app.tradogt.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Data
@Entity
@Table(name = "MyFavorite")
public class MyFavorite {
    @EmbeddedId
    private MyFavoriteId id;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "productoEnZona_id", nullable = false, referencedColumnName = "producto_idProducto")
    private ProductoEnZona productoEnZona;

    @Column(name = "fecha")
    private Instant fecha;

}
