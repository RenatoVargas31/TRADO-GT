package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Rol_idRol", nullable = false)
    private Rol rolIdrol;

    @ManyToOne
    @JoinColumn(name = "AdmZonal_idUsuario")
    private Usuario admzonalIdusuario;

    @ManyToOne
    @JoinColumn(name = "AgentCompra_idUsuario")
    private Usuario agentcompraIdusuario;

    @ManyToOne
    @JoinColumn(name = "Zona_idZona")
    private Zona zonaIdzona;

    @ManyToOne
    @JoinColumn(name = "Distrito_idDistrito")
    private Distrito distritoIddistrito;

    @Size(max = 45)
    @NotNull
    @Column(name = "Nombre", nullable = false, length = 45)
    private String nombre;

    @Size(max = 45)
    @NotNull
    @Column(name = "Apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "FechaNacimiento")
    private LocalDate fechaNacimiento;

    @Size(max = 8)
    @Column(name = "Dni", length = 8)
    private String dni;

    @Size(max = 9)
    @Column(name = "Telefono", length = 9)
    private String telefono;

    @Size(max = 45)
    @Column(name = "Correo", length = 45)
    private String correo;

    @Size(max = 45)
    @Column(name = "Contrasena", length = 45)
    private String contrasena;

    @Size(max = 45)
    @Column(name = "Ruc", length = 45)
    private String ruc;

    @Size(max = 45)
    @Column(name = "RazonSocial", length = 45)
    private String razonSocial;

    @Size(max = 45)
    @Column(name = "Direccion", length = 45)
    private String direccion;

    @ColumnDefault("0")
    @Column(name = "isAccepted")
    private Byte isAccepted;

    @ColumnDefault("0")
    @Column(name = "isPostulated")
    private Byte isPostulated;

    @ColumnDefault("0")
    @Column(name = "isActivated")
    private Byte isActivated;

    @Size(max = 45)
    @Column(name = "Foto", length = 45)
    private String foto;

    @Size(max = 45)
    @Column(name = "MotivoBanneo", length = 45)
    private String motivoBanneo;

    @Column(name = "FechaBanneo")
    private Instant fechaBanneo;

}
