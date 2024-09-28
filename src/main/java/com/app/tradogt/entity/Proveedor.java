package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProveedor", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

    @Size(max = 9)
    @NotNull
    @Column(name = "Telefono", nullable = false, length = 9)
    private String telefono;

    @Size(max = 45)
    @NotNull
    @Column(name = "Ruc", nullable = false, length = 45)
    private String ruc;

    @Size(max = 8)
    @NotNull
    @Column(name = "Dni", nullable = false, length = 8)
    private String dni;

    @Size(max = 45)
    @NotNull
    @Column(name = "Tienda", nullable = false, length = 45)
    private String tienda;

    @ColumnDefault("0")
    @Column(name = "isDeleted")
    private Byte isDeleted;

}
