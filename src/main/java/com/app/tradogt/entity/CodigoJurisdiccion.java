package com.app.tradogt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "CodigoJurisdiccion")
public class CodigoJurisdiccion {
    @Id
    @Column(name = "idCodigoJurisdiccion", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private Integer codigo;

}