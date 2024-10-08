package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Carrito")
public class Carrito {
    @EmbeddedId
    private CarritoId id;

    @MapsId("productoEnZonaId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ProductoEnZona_producto_idProducto", referencedColumnName = "producto_idProducto", nullable = false),
            @JoinColumn(name = "ProductoEnZona_zona_idZona", referencedColumnName = "zona_idZona", nullable = false)
    })
    private ProductoEnZona productoEnZona;

    @MapsId("usuarioIdusuario")
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "costo")
    private BigDecimal costo;

    @ColumnDefault("0")
    @Column(name = "isDelete")
    private Byte isDelete;

    @ColumnDefault("0.00")
    @Column(name = "totalCosto", precision = 10, scale = 2)
    private BigDecimal costoTotal;

    
    @ManyToOne
    @JoinColumn(name = "Orden_idOrden")
    private Orden ordenIdorden;

}