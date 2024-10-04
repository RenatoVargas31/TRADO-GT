package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "CodigoDespachador")
public class CodigoDespachador {
    @Id
    @Column(name = "idCodigoDespachador", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "estadoCodigo_idEstadoCodigo", nullable = false)
    private EstadoCodigo estadocodigoIdestadocodigo;

}
