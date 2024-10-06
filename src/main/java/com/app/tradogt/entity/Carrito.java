package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "Carrito")
public class Carrito {
    @EmbeddedId
    private CarritoId id;

    @MapsId("productoEnZonaId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "ProductoEnZona_producto_idProducto", referencedColumnName = "producto_idProducto", nullable = false),
            @JoinColumn(name = "ProductoEnZona_zona_idZona", referencedColumnName = "zona_idZona", nullable = false)
    })
    private ProductoEnZona productoEnZona;

    @MapsId("usuarioIdusuario")
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    
    @ManyToOne
    @JoinColumn(name = "Orden_idOrden")
    private Orden ordenIdorden;

}