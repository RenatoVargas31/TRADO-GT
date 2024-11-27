package com.app.tradogt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRecoverDto {

    @NotBlank(message = "La nueva contraseña es obligatoria.")
    @Size(min = 8, max = 16, message = "La nueva contraseña debe tener entre 8 y 16 caracteres.")
    @Pattern(regexp = "^(?=.*[0-9])(?=(.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]){2,})(?=.*[A-Z])(?=.*[a-z]).{8,16}$",
            message = "La nueva contraseña debe contener al menos 1 número, 2 caracteres especiales y 1 letra mayúscula y minúscula.")
    private String newPassword;

    @NotBlank(message = "Debe confirmar su nueva contraseña.")
    private String confirmNewPassword;
}
