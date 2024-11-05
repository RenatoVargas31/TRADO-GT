package com.app.tradogt.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationCorreoService {

    @Autowired
    private JavaMailSender mailSender;

    // Método para enviar correo de notificación al usuario
    public void enviarCorreoCambioContraseña(String destinatario, String nombreUsuario) {
        String asunto = "Cambio de Contraseña Exitoso";
        String mensajeHtml = "<h1>Hola " + nombreUsuario + ",</h1>" +
                "<p>Tu contraseña ha sido cambiada exitosamente.</p>" +
                "<p>Si no solicitaste este cambio, por favor contacta a nuestro equipo de soporte de inmediato.</p>" +
                "<br><p>Saludos,<br>El equipo de soporte</p>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Configuración del correo
            helper.setSubject(asunto);
            helper.setTo(destinatario);
            helper.setText(mensajeHtml, true); // true permite contenido HTML

            // Enviar el correo (el remitente se toma de spring.mail.username automáticamente)
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo de cambio de contraseña", e);
        }
    }
}
