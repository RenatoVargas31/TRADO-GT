package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioOrdenAgtDto {

    private String dni;
    private String nombre;
    private String apellido;
    private String distrito;
    private String correo;
    private Integer idUsuario;
    private Integer idOrden;

    public UsuarioOrdenAgtDto(String dni, String nombre, String apellido, String distrito, String correo, Integer idUsuario, Integer idOrden) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.distrito = distrito;
        this.correo = correo;
        this.idUsuario = idUsuario;
        this.idOrden = idOrden;
    }

    public UsuarioOrdenAgtDto() {
    }
}
