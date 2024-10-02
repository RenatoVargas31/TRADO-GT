package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProveedorInfoAgtDto {

    private Integer idProveedor;
    private String nombreProvedor;
    private String telefonoProovedor;
    private String nombreTienda;

    public ProveedorInfoAgtDto(Integer idProveedor, String nombreProvedor, String telefonoProovedor, String nombreTienda) {
        this.idProveedor = idProveedor;
        this.nombreProvedor = nombreProvedor;
        this.telefonoProovedor = telefonoProovedor;
        this.nombreTienda = nombreTienda;
    }

    public ProveedorInfoAgtDto() {
    }
}
