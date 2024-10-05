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
@Table(name = "Proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProveedor", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Size(max = 9)
    @NotNull
    @Column(name = "telefono", nullable = false, length = 9)
    private String telefono;

    @Size(max = 45)
    @NotNull
    @Column(name = "ruc", nullable = false, length = 45)
    private String ruc;

    @Size(max = 8)
    @NotNull
    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @Size(max = 45)
    @NotNull
    @Column(name = "tienda", nullable = false, length = 45)
    private String tienda;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isDeleted", nullable = false)
    private Byte isDeleted = 0;

    @Size(max = 45)
    @NotNull
    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

}
