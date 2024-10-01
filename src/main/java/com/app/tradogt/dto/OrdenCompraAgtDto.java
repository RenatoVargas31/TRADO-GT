package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenCompraAgtDto {

    private String usuarioPropietario;
    private String fechaCreacion;  // En formato dd-mm-yyyy
    private String metodoPago;
    private String agenteCompra;
    private String estadoPedido;

    // Constructor opcional para inicializar los campos
    public OrdenCompraAgtDto(String usuarioPropietario, String fechaCreacion, String metodoPago, String agenteCompra, String estadoPedido) {
        this.usuarioPropietario = usuarioPropietario;
        this.fechaCreacion = fechaCreacion;
        this.metodoPago = metodoPago;
        this.agenteCompra = agenteCompra;
        this.estadoPedido = estadoPedido;
    }

    // Constructor vacío
    public OrdenCompraAgtDto() {
    }
}
