package com.app.tradogt.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {
    private String nombre;
    private String apellido;
    private String codigoDespachador;

    public CustomUserDetails(String username, String password, boolean enabled,
                             String nombre, String apellido, String codigoDespachador,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.nombre = nombre;
        this.apellido = apellido;
        this.codigoDespachador = codigoDespachador;
    }
}

