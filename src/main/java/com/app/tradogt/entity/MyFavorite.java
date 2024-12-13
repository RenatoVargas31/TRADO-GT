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

    @MapsId("usuarioIdusuario")
    @ManyToOne
    @JoinColumn(name = "usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @MapsId("productoEnZona")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "producto_idProducto", referencedColumnName = "producto_idProducto", nullable = false),
            @JoinColumn(name = "zona_idZona", referencedColumnName = "zona_idZona", nullable = false)
    })
    private ProductoEnZona productoEnZona;

    @Column(name = "fecha")
    private Instant fecha;

}
