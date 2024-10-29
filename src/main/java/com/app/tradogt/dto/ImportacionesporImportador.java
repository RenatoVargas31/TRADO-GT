package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportacionesporImportador {

    private String tienda;
    private long totalProductos;
    private double sumaCantidades;


    public ImportacionesporImportador(String tienda, long totalProductos, double sumaCantidades) {
        this.tienda = tienda;
        this.totalProductos = totalProductos;
        this.sumaCantidades = sumaCantidades;
    }

    public ImportacionesporImportador() {
    }
}
