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

    @MapsId("productoEnZonaId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "productoEnZona_Producto_idProducto", referencedColumnName = "producto_idProducto", nullable = false),
            @JoinColumn(name = "productoEnZona_Zona_idZona", referencedColumnName = "zona_idZona", nullable = false)
    })
    private ProductoEnZona productoEnZona;

    @MapsId("ordenIdorden")
    @ManyToOne
    @JoinColumn(name = "orden_idOrden", nullable = false)
    private Orden ordenIdorden;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
