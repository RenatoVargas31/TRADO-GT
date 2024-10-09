package com.app.tradogt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordChangeDto {

    @NotBlank(message = "La contraseña actual es obligatoria.")
    private String currentPassword;

    @NotBlank(message = "La nueva contraseña es obligatoria.")
    @Size(min = 8, max = 16, message = "La nueva contraseña debe tener entre 8 y 16 caracteres.")
    private String newPassword;

    @NotBlank(message = "Debe confirmar su nueva contraseña.")
    private String confirmNewPassword;

    // Getters y Setters

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
