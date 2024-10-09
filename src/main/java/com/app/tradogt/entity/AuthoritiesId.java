package com.app.tradogt.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class AuthoritiesId implements Serializable {

    private Integer idUsuario;
    private String authority;
}