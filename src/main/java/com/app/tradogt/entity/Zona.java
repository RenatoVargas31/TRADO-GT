package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idZona", nullable = false)
    private Integer id;

    @Size(max = 10)
    @NotNull
    @Column(name = "nameZona", nullable = false, length = 10)
    private String nameZona;

}
