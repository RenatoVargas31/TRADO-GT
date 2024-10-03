package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "estadoordenimportador")
public class EstadoOrdenImportador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEstadoOrdenImportador", nullable = false)
    private Integer id;

    @Size(max = 45)
    @Column(name = "Nombre", length = 45)
    private String nombre;

}
