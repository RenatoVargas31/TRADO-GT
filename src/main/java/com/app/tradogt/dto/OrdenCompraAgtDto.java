package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrdenCompraAgtDto {

    private String usuarioPropietario;
    private Integer idUsuarioAsignado;
    private String fechaCreacion;  // En formato dd-mm-yyyy
    private BigDecimal montoTotal;
    private String codigoOrden;
    private Long idAgenteAsignado;
    private String estadoPedido;
    private Integer idOrden;

    public OrdenCompraAgtDto(String usuarioPropietario, Integer idUsuarioAsignado, String fechaCreacion, BigDecimal montoTotal, String codigoOrden, Long idAgenteAsignado, String estadoPedido, Integer idOrden) {
        this.usuarioPropietario = usuarioPropietario;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.fechaCreacion = fechaCreacion;
        this.montoTotal = montoTotal;
        this.codigoOrden = codigoOrden;
        this.idAgenteAsignado = idAgenteAsignado;
        this.estadoPedido = estadoPedido;
        this.idOrden = idOrden;
    }

    // Constructor vacío
    public OrdenCompraAgtDto() {
    }
}
