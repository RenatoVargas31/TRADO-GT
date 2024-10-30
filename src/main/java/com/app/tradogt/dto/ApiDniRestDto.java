package com.app.tradogt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiDniRestDto {

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String digitoVerificador;
    private String message;
}
