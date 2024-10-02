package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDetailsAgtDto {

    private Integer idProducto;
    private String nombreProducto;
    private BigDecimal precioProducto;
    private Integer cantidadProducto;
    private BigDecimal costoEnvio;

    public ProductDetailsAgtDto(Integer idProducto, String nombreProducto, BigDecimal precioProducto, Integer cantidadProducto, BigDecimal costoEnvio) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.cantidadProducto = cantidadProducto;
        this.costoEnvio = costoEnvio;
    }

    public ProductDetailsAgtDto() {
    }
}
