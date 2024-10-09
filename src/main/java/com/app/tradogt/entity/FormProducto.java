package com.app.tradogt.entity;

import lombok.Data;

@Data
public class FormProducto {
    private Producto producto;
    private ProductoEnZona productoEnZonaNorte;
    private ProductoEnZona productoEnZonaSur;
    private ProductoEnZona productoEnZonaEste;
    private ProductoEnZona productoEnZonaOeste;
}
