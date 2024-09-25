package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuarios", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idCoordinador")
    private Usuario idCoordinador;

    @ManyToOne
    @JoinColumn(name = "idAgente")
    private Usuario idAgente;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "roles_idRoles", nullable = false)
    private Rol rolesIdroles;

    @Size(max = 45)
    @Column(name = "nombreUsuario", length = 45)
    private String nombreUsuario;

    @Size(max = 45)
    @Column(name = "apellidoUsuario", length = 45)
    private String apellidoUsuario;

    @Size(max = 8)
    @Column(name = "dniUsuario", length = 8)
    private String dniUsuario;

    @Size(max = 45)
    @NotNull
    @Column(name = "correoUsuario", nullable = false, length = 45)
    private String correoUsuario;

    @Column(name = "FechaNacimiento")
    private LocalDate fechaNacimiento;

    @Size(max = 256)
    @NotNull
    @Column(name = "contrasenaUsuario", nullable = false, length = 256)
    private String contrasenaUsuario;

    @Size(max = 9)
    @Column(name = "telefonoUsuario", length = 9)
    private String telefonoUsuario;

    @Size(max = 80)
    @Column(name = "direccionUsuario", length = 80)
    private String direccionUsuario;

    @Size(max = 20)
    @Column(name = "codigoDespachador", length = 20)
    private String codigoDespachador;

    @Size(max = 11)
    @Column(name = "rucUsuario", length = 11)
    private String rucUsuario;

    @Size(max = 45)
    @Column(name = "razonSocial", length = 45)
    private String razonSocial;

    @Size(max = 45)
    @Column(name = "codigoJurisdiccion", length = 45)
    private String codigoJurisdiccion;

    @ManyToOne
    @JoinColumn(name = "estadoCodigoDespachador_idEstado")
    private EstadoCodigo estadocodigodespachadorIdestado;

    @ManyToOne
    @JoinColumn(name = "zonas_idZona")
    private Zona zonasIdzona;

    @ManyToOne
    @JoinColumn(name = "distritos_idDistrito")
    private Distrito distritosIddistrito;

    @Size(max = 100)
    @Column(name = "solicitudAgente", length = 100)
    private String solicitudAgente;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_active", nullable = false)
    private Byte isActive;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isAccepted", nullable = false)
    private Byte isAccepted;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "postulaAgente", nullable = false)
    private Byte postulaAgente;

}
