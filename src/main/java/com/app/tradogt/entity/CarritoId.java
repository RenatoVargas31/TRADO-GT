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
public class CarritoId implements Serializable {
    @NotNull
    @Column(name = "ProductoEnZona_producto_idProducto", nullable = false)
    private Integer productoenzonaProductoIdproducto;

    @NotNull
    @Column(name = "ProductoEnZona_zona_idZona", nullable = false)
    private Integer productoenzonaZonaIdzona;

    @NotNull
    @Column(name = "Usuario_idUsuario", nullable = false)
    private Integer usuarioIdusuario;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CarritoId entity = (CarritoId) o;
        return Objects.equals(this.usuarioIdusuario, entity.usuarioIdusuario) &&
                Objects.equals(this.productoenzonaZonaIdzona, entity.productoenzonaZonaIdzona) &&
                Objects.equals(this.productoenzonaProductoIdproducto, entity.productoenzonaProductoIdproducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioIdusuario, productoenzonaZonaIdzona, productoenzonaProductoIdproducto);
    }

}