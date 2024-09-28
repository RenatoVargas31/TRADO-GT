package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "productoEnZona")
public class ProductoEnZona {
    @EmbeddedId
    private ProductoEnZonaId id;

    @MapsId("productoIdproducto")
    @ManyToOne
    @JoinColumn(name = "Producto_idProducto", nullable = false)
    private Producto productoIdproducto;

    @MapsId("zonaIdzona")
    @ManyToOne
    @JoinColumn(name = "Zona_idZona", nullable = false)
    private Zona zonaIdzona;

    @NotNull
    @Column(name = "Cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "CostoEnvio", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEnvio;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "estadoRepo", nullable = false)
    private Byte estadoRepo;

}
