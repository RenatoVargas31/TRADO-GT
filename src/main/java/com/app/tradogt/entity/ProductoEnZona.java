package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
@Getter
@Setter
@Entity
@Table(name = "ProductoEnZona")
public class ProductoEnZona {
    @EmbeddedId
    private ProductoEnZonaId id;

    @MapsId("productoIdproducto")
    @ManyToOne
    @JoinColumn(name = "producto_idProducto", nullable = false)
    private Producto productoIdproducto;

    @MapsId("zonaIdzona")
    @ManyToOne
    @JoinColumn(name = "zona_idZona", nullable = false)
    private Zona zonaIdzona;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "costoEnvio", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEnvio;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "estadoRepo", nullable = false)
    private Byte estadoRepo = 0;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isDeleted", nullable = false)
    private Byte isDeleted = 0;

    @Column(name = "contar")
    private Integer contar = 0;

    public void setProductoyZona (Producto producto,Zona zona){
        this.id = new ProductoEnZonaId(producto.getId(),zona.getId());
        this.productoIdproducto = producto;
        this.zonaIdzona = zona;
    }
}
