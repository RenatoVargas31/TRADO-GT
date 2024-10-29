package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoStock {

    private String nombre;
    private long stock;



    public ProductoStock(String nombre, long stock) {
        this.nombre = nombre;
        this.stock=stock;
    }

    public ProductoStock() {
    }
}
