package com.app.tradogt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanUserDto {

    private int idImportador;

    @NotBlank(message = "La razón del baneo es obligatoria.")
    @Size(min = 10, max = 300, message = "La razón del baneo debe tener entre 10 y 300 caracteres.")
    private String banReason;

}
