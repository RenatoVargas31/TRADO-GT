package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "distritos")
public class Distrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDistrito", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "nameDistrito", nullable = false, length = 45)
    private String nameDistrito;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "zonas_idZona", nullable = false)
    private Zona zonasIdzona;

}
