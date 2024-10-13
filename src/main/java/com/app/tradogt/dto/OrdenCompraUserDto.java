package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OrdenCompraUserDto {
    private Integer idOrden;
    private String codigoOrden;
    private BigDecimal costoTotal;
    private LocalDate fechaCreacion;
    private String estadoOrden;
    private String agente;
    private Integer valoracion;

    // Constructor
   public OrdenCompraUserDto(Integer idOrden, String codigoOrden, BigDecimal costoTotal, LocalDate fechaCreacion, String estadoOrden, String agente, Integer valoracion) {
        this.idOrden = idOrden;
        this.codigoOrden = codigoOrden;
        this.costoTotal = costoTotal;
        this.fechaCreacion = fechaCreacion;
        this.estadoOrden = estadoOrden;
        this.agente = agente;
        this.valoracion = valoracion;
    }

}

