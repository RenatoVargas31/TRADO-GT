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
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombreProveedor", nullable = false, length = 45)
    private String nombreProveedor;

    @Size(max = 45)
    @NotNull
    @Column(name = "apellidoProveedor", nullable = false, length = 45)
    private String apellidoProveedor;

    @Size(max = 9)
    @NotNull
    @Column(name = "telefonoProveedor", nullable = false, length = 9)
    private String telefonoProveedor;

    @Size(max = 11)
    @NotNull
    @Column(name = "rucProveedor", nullable = false, length = 11)
    private String rucProveedor;

    @Size(max = 8)
    @NotNull
    @Column(name = "dniProveedor", nullable = false, length = 8)
    private String dniProveedor;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombreTienda", nullable = false, length = 45)
    private String nombreTienda;


    @NotNull
    @ColumnDefault("1")
    @Column(name = "enabled", nullable = false)
    private Byte enabled;

}
