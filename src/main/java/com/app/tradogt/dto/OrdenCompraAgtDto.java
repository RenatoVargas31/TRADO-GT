package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenCompraAgtDto {

    private String usuarioPropietario;
    private Integer idUsuarioAsignado;
    private String fechaCreacion;  // En formato dd-mm-yyyy
    private String metodoPago;
    private String agenteCompra;
    private Long idAgenteAsignado;
    private String estadoPedido;
    private Integer idOrden;

    // Constructor opcional para inicializar los campos
    public OrdenCompraAgtDto(String usuarioPropietario, Integer idUsuarioAsignado, String fechaCreacion, String metodoPago, String agenteCompra, Long idAgenteAsignado, String estadoPedido, Integer idOrden) {
        this.usuarioPropietario = usuarioPropietario;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.fechaCreacion = fechaCreacion;
        this.metodoPago = metodoPago;
        this.agenteCompra = agenteCompra;
        this.idAgenteAsignado = idAgenteAsignado;
        this.estadoPedido = estadoPedido;
        this.idOrden = idOrden;
    }


    // Constructor vacío
    public OrdenCompraAgtDto() {
    }
}
