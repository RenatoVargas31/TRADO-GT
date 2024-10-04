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
public class ProductoEnZonaId implements Serializable {
    @NotNull
    @Column(name = "Producto_idProducto", nullable = false)
    private Integer productoIdproducto;

    @NotNull
    @Column(name = "Zona_idZona", nullable = false)
    private Integer zonaIdzona;
}