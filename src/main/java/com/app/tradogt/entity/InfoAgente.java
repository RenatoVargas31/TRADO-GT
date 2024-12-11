package com.app.tradogt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "InfoAgente")
public class InfoAgente {
    @Id
    @Size(max = 45)
    @Column(name = "codigoAduanero", nullable = false, length = 45)
    private String codigoAduanero;

    @Size(max = 45)
    @NotNull
    @Column(name = "codigoJuris", nullable = false, length = 45)
    private String codigoJuris;

    @Size(max = 45)
    @NotNull
    @Column(name = "estado", nullable = false, length = 45)
    private String estado;

}
