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
@Table(name = "estadocodigos")
public class EstadoCodigo {
    @Id
    @Column(name = "idEstado", columnDefinition = "int UNSIGNED not null")
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombreEstado", nullable = false, length = 45)
    private String nombreEstado;

}