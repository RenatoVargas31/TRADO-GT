package com.app.tradogt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class ProductoEnZonaId implements Serializable {
    @NotNull
    @Column(name = "Producto_idProducto", nullable = false)
    private Integer productoIdproducto;

    @NotNull
    @Column(name = "Zona_idZona", nullable = false)
    private Integer zonaIdzona;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductoEnZonaId entity = (ProductoEnZonaId) o;
        return Objects.equals(this.productoIdproducto, entity.productoIdproducto) &&
                Objects.equals(this.zonaIdzona, entity.zonaIdzona);
    }
    @Override
    public int hashCode() {
        return Objects.hash(productoIdproducto, zonaIdzona);
    }
}