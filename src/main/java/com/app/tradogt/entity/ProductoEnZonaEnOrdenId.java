package com.app.tradogt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProductoEnZonaEnOrdenId implements Serializable {

    @NotNull
    @Column(name = "ProductoEnZona_Producto_idProducto", nullable = false)
    private Integer productoenzonaProductoIdproducto;

    @NotNull
    @Column(name = "ProductoEnZona_Zona_idZona", nullable = false)
    private Integer productoenzonaZonaIdzona;

    @NotNull
    @Column(name = "Orden_idOrden", nullable = false)
    private Integer ordenIdorden;


}