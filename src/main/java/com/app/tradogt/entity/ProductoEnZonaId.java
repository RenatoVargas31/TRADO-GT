package com.app.tradogt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class ProductoEnZonaId implements Serializable {
    @NotNull
    @Column(name = "Producto_idProducto", nullable = false)
    private Integer productoIdproducto;

    @NotNull
    @Column(name = "Zona_idZona", nullable = false)
    private Integer zonaIdzona;
}