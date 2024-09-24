package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "proveedores_id_proveedor")
    private Proveedor proveedoresIdProveedor;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombreProducto", nullable = false, length = 45)
    private String nombreProducto;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "`categoria_idCategoria`", nullable = false)
    private Categoria categoriaIdcategoria;

    @Size(max = 250)
    @NotNull
    @Column(name = "desccripcion", nullable = false, length = 250)
    private String desccripcion;

    @NotNull
    @Column(name = "precio", nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

    @NotNull
    @Column(name = "costoEnvio", nullable = false, precision = 8, scale = 2)
    private BigDecimal costoEnvio;

    @Size(max = 45)
    @Column(name = "paisOrigen", length = 45)
    private String paisOrigen;

    @Size(max = 45)
    @Column(name = "colores", length = 45)
    private String colores;

    @Size(max = 45)
    @Column(name = "talla", length = 45)
    private String talla;

    @Size(max = 45)
    @Column(name = "material", length = 45)
    private String material;

    @Size(max = 45)
    @Column(name = "modelo", length = 45)
    private String modelo;

    @Size(max = 45)
    @Column(name = "resolucion", length = 45)
    private String resolucion;

    @Column(name = "ram")
    private Integer ram;

    @Column(name = "almacenamiento")
    private Integer almacenamiento;

}