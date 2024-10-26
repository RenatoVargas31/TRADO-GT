package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rol_idRol", nullable = false)
    private Rol rolIdrol;

    @ManyToOne
    @JoinColumn(name = "admZonal_idUsuario")
    private Usuario admzonalIdusuario;

    @ManyToOne
    @JoinColumn(name = "agentCompra_idUsuario")
    private Usuario agentcompraIdusuario;

    @ManyToOne
    @JoinColumn(name = "zona_idZona")
    private Zona zonaIdzona;

    @ManyToOne
    @JoinColumn(name = "distrito_idDistrito")
    private Distrito distritoIddistrito;

    @Size(max = 45)
    @Column(name = "foto", length = 45)
    private String foto;

    @Size(max = 45)
    @NotNull
    @NotBlank(message = "Campo obligatorio")
    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Size(max = 45)
    @NotNull
    @NotBlank(message = "Campo obligatorio")
    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;

    @Size(max = 8)
    @Digits(integer = 8, fraction = 0, message = "El DNI debe estar compuesto por 8 dígitos")
    @NotBlank(message = "El documento de identidad es obligatoria")
    @Column(name = "dni", length = 8)
    private String dni;

    @Size(max = 9)
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp ="^\\+?[0-9. ()-]{7,25}$", message = "El teléfono tiene un formato inválido")
    @Digits(integer = 9, fraction = 0)
    @Column(name = "telefono", length = 9)
    private String telefono;

    @Email
    @Size(max = 45)
    @Column(name = "correo", length = 45)
    private String correo;

    @Size(max = 120)
    @Column(name = "contrasena", length = 80)
    private String contrasena;

    @Size(max = 10)
    @Column(name = "ruc", length = 10)
    private String ruc;

    @Size(max = 45)
    @Column(name = "razonSocial", length = 45)
    private String razonSocial;

    @Size(max = 45)
    @Size(max = 45, message = "La dirección no puede tener más de 45 caracteres")
    @Column(name = "direccion", length = 45)
    private String direccion;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isAccepted", nullable = false)
    private Byte isAccepted = 0;

    @ColumnDefault("0")
    @Column(name = "isPostulated")
    private Byte isPostulated = 0;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isActivated", nullable = false)
    private Byte isActivated = 0;

    @Size(max = 45)
    @Column(name = "codigoDespachador", length = 45)
    private String codigoDespachador;

    @Size(max = 150)
    @Column(name = "motivoBaneo", length = 150)
    private String motivoBaneo;

}
