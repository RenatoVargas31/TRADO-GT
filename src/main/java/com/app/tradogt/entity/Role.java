package com.app.tradogt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRoles", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "nombreRol", nullable = false, length = 45)
    private String nombreRol;

}