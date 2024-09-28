package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Proveedor_idProveedor", nullable = false)
    private Proveedor proveedorIdproveedor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SubCategoria_idSubCategoria", nullable = false)
    private Subcategoria subcategoriaIdsubcategoria;

    @Size(max = 45)
    @NotNull
    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

    @Size(max = 45)
    @NotNull
    @Column(name = "Cantidad", nullable = false, length = 45)
    private String cantidad;

    @Size(max = 45)
    @NotNull
    @Column(name = "FechaArribo", nullable = false, length = 45)
    private String fechaArribo;

    @Size(max = 45)
    @NotNull
    @Column(name = "Descripcion", nullable = false, length = 45)
    private String descripcion;

    @Size(max = 45)
    @NotNull
    @Column(name = "Precio", nullable = false, length = 45)
    private String precio;

    @Size(max = 45)
    @Column(name = "Color", length = 45)
    private String color;

    @Size(max = 45)
    @Column(name = "Talla", length = 45)
    private String talla;

    @Size(max = 45)
    @Column(name = "Material", length = 45)
    private String material;

    @Size(max = 45)
    @Column(name = "Modelo", length = 45)
    private String modelo;

    @Size(max = 45)
    @Column(name = "Resolucion", length = 45)
    private String resolucion;

    @Size(max = 45)
    @Column(name = "Ram", length = 45)
    private String ram;

    @Size(max = 45)
    @Column(name = "Almacenamiento", length = 45)
    private String almacenamiento;

    @ColumnDefault("0")
    @Column(name = "isDeleted")
    private Byte isDeleted;

}