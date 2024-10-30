package com.app.tradogt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductoMasVendidoDto {
    private String nombre;
    private Long cantidadVendida;
}
