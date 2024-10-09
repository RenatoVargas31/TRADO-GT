package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "ProductoEnCarrito")
public class ProductoEnCarrito {
    @EmbeddedId
    private ProductoEnCarritoId id;

    @MapsId("productoEnZona")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ProductoEnZona_producto_idProducto", referencedColumnName = "producto_idProducto", nullable = false),
            @JoinColumn(name = "ProductoEnZona_zona_idZona", referencedColumnName = "zona_idZona", nullable = false)
    })
    private ProductoEnZona productoEnZona;

    @MapsId("carritoIdcarrito")
    @ManyToOne
    @JoinColumn(name = "Carrito_idCarrito", nullable = false)
    private Carrito carritoIdcarrito;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

}
