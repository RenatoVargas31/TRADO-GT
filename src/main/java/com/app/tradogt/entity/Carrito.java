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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarrito", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @ColumnDefault("0")
    @Column(name = "isDelete")
    private Byte isDelete;

    @ColumnDefault("0.00")
    @Column(name = "costoTotal", precision = 10, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "costoEnvio")
    private BigDecimal costoEnvio;

}