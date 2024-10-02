package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@Table(name = "orden")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrden", nullable = false)
    private Integer id;

    // Relación uno a muchos con Pago
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuarioIdusuario;

    @ManyToOne
    @JoinColumn(name = "AgentCompra_idUsuario")
    private Usuario agentcompraIdusuario;

    @ManyToOne
    @JoinColumn(name = "Valoracion_idValoracion")
    private Valoracion valoracionIdvaloracion;

    @ManyToOne
    @JoinColumn(name = "EstadoOrdenAgente_idEstadoOrdenAgente")
    private EstadoOrdenAgente estadoordenagenteIdestadoordenagente;

    @ManyToOne
    @JoinColumn(name = "EstadoOrdenImportador_idEstadoOrdenImportador")
    private EstadoOrdenImportador estadoordenimportadorIdestadoordenimportador;

    @NotNull
    @Column(name = "FechaArribo")
    private Instant fechaArribo;

    @NotNull
    @Column(name = "FechaCreacion", nullable = false)
    private Instant fechaCreacion;

    @ColumnDefault("0")
    @Column(name = "isDeleted")
    private Byte isDeleted;


}
