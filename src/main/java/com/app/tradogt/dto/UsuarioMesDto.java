package com.app.tradogt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Data
public class UsuarioMesDto {
    private LocalDateTime fechaRegistro;
    private Byte isActivated;
}
