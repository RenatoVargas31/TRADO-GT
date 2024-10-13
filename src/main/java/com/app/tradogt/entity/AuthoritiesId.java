package com.app.tradogt.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Data
public class AuthoritiesId implements Serializable {

    private Integer idUsuario;
    private String authority;
}