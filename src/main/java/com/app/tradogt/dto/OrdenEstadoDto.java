package com.app.tradogt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class OrdenEstadoDto {
    private LocalDate fechaCreacion;
    private LocalDate fechaValidacion;
    private LocalDate fechaEnProceso;
    private LocalDate fechaArribo;
    private LocalDate fechaEnAduanas;
    private LocalDate fechaEnRuta;
    private LocalDate fechaRecibido;
}
