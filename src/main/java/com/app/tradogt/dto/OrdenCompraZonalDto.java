package com.app.tradogt.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class OrdenCompraZonalDto {
    private String usuarioPropietario;
    private Integer idUsuarioAsignado;
    private Date fechaCreacion;  // En formato dd-mm-yyyy
    private Date fechaRecibido;  // En formato dd-mm-yyyy
    private BigDecimal montoTotal;
    private String codigoOrden;
    private Long idAgenteAsignado;
    private String estadoPedido;
    private Integer idOrden;
    private String nombreDistrito;

    public OrdenCompraZonalDto(String usuarioPropietario, Integer idUsuarioAsignado, Date fechaCreacion, Date fechaRecibido, BigDecimal montoTotal, String codigoOrden, Long idAgenteAsignado, String estadoPedido, Integer idOrden, String nombreDistrito) {
        this.usuarioPropietario = usuarioPropietario;
        this.idUsuarioAsignado = idUsuarioAsignado;
        this.fechaCreacion = fechaCreacion;
        this.fechaRecibido = fechaRecibido;
        this.montoTotal = montoTotal;
        this.codigoOrden = codigoOrden;
        this.idAgenteAsignado = idAgenteAsignado;
        this.estadoPedido = estadoPedido;
        this.idOrden = idOrden;
        this.nombreDistrito = nombreDistrito;
    }

    public OrdenCompraZonalDto() {
    }
}
