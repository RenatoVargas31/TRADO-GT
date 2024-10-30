package com.app.tradogt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Data
public class UsuarioMesDto {
    private LocalDate fechaRegistro;
    private Byte isActivated;
}
