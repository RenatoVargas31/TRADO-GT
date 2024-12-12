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
@Table(name = "Producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "proveedor_idProveedor", nullable = false)
    private Proveedor proveedorIdproveedor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subCategoria_idSubCategoria", nullable = false)
    private SubCategoria subcategoriaIdsubcategoria;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Size(max = 250)
    @NotNull
    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    @NotNull
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Size(max = 45)
    @NotNull
    @Column(name = "color", nullable = false, length = 45)
    private String color;

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

    @Size(max = 45)
    @Column(name = "ram", length = 45)
    private String ram;

    @Size(max = 45)
    @Column(name = "almacenamiento", length = 45)
    private String almacenamiento;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isDeleted", nullable = false)
    private Byte isDeleted = 0;

    @NotNull
    @Column(name = "peso", nullable = false, precision = 10, scale = 2)
    private BigDecimal peso;

    @Size(max = 45)
    @NotNull
    @Column(name = "marca", nullable = false, length = 45)
    private String marca;

    @Size(max = 45)
    @Column(name = "ancho", length = 45)
    private String ancho;

    @Size(max = 45)
    @Column(name = "alto", length = 45)
    private String alto;

    @Size(max = 45)
    @Column(name = "profundidad", length = 45)
    private String profundidad;

    @Size(max = 45)
    @Column(name = "codigo", nullable = false, length = 45)
    private String codigo;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Lob
    @Basic(fetch = FetchType.LAZY) // Opcional: si prefieres cargarlo bajo demanda
    @Column(name = "foto")
    private byte[] foto;

}