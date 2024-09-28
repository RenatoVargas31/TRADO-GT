package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "ProductoEnZonaEnOrden")
public class ProductoEnZonaEnOrden {
    @EmbeddedId
    private ProductoEnZonaEnOrdenId id;

    @MapsId("productoenzonaId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ProductoEnZona_Producto_idProducto", referencedColumnName = "Producto_idProducto", nullable = false),
            @JoinColumn(name = "ProductoEnZona_Zona_idZona", referencedColumnName = "Zona_idZona", nullable = false)
    })
    private ProductoEnZona productoEnZona;

    @MapsId("ordenIdorden")
    @ManyToOne
    @JoinColumn(name = "Orden_idOrden", nullable = false)
    private Orden ordenIdorden;

    @NotNull
    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

}
