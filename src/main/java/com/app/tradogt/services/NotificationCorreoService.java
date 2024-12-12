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
        String mensajeHtml = "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "</head>" +
                "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "        <tr>" +
                "            <td align='center' style='padding: 40px 0; background-color: #800080; color: #ffffff;'>" +
                "                <h1 style='margin: 0; font-size: 24px;'>Hola " + nombreUsuario + ",</h1>" +
                "            </td>" +
                "        </tr>" +
                "        <tr>" +
                "            <td align='center' style='padding: 20px;'>" +
                "                <table width='600' border='0' cellspacing='0' cellpadding='0' style='background-color: #ffffff; border-radius: 8px; padding: 40px;'>" +
                "                    <tr>" +
                "                        <td align='center' style='padding: 20px; color: #333333;'>" +
                "                            <p style='font-size: 16px; line-height: 1.5;'>Tu contraseña ha sido cambiada exitosamente.</p>" +
                "                            <p style='font-size: 16px; line-height: 1.5; color: #b30000;'>Si no solicitaste este cambio, por favor contacta a nuestro equipo de soporte de inmediato.</p>" +
                "                            <a href='mailto:serviciotecnico.trado@gmail.com' style='display: inline-block; margin-top: 20px; padding: 12px 24px; background-color: #800080; color: #ffffff; text-decoration: none; border-radius: 5px; font-size: 16px;'>Contactar Soporte</a>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "        <tr>" +
                "            <td align='center' style='padding: 20px; color: #888888; font-size: 12px;'>" +
                "                <p>Saludos,<br>El equipo de soporte</p>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";


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

    // Método para enviar correo de notificación para recuperación de contraseña
    public void enviarCorreoRecuperacion(String destinatario, String nombreUsuario, String token) {
        String asunto = "Recuperación de Contraseña";
        String mensajeHtml = "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "</head>" +
                "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "        <tr>" +
                "            <td align='center' style='padding: 40px 0; background-color: #800080; color: #ffffff;'>" +
                "                <h1 style='margin: 0; font-size: 24px;'>Hola " + nombreUsuario + ",</h1>" +
                "            </td>" +
                "        </tr>" +
                "        <tr>" +
                "            <td align='center' style='padding: 20px;'>" +
                "                <table width='600' border='0' cellspacing='0' cellpadding='0' style='background-color: #ffffff; border-radius: 8px; padding: 40px;'>" +
                "                    <tr>" +
                "                        <td align='center' style='padding: 20px; color: #333333;'>" +
                "                            <p style='font-size: 16px; line-height: 1.5;'>Recibimos una solicitud para restablecer tu contraseña.</p>" +
                "                            <p style='font-size: 16px; line-height: 1.5;'>Tu código de recuperación es:</p>" +
                "                            <h2 style='color: #800080;'>" + token + "</h2>" +
                "                            <p style='font-size: 16px; line-height: 1.5;'>Este código es válido por 30 minutos.</p>" +
                "                            <p style='font-size: 16px; line-height: 1.5; color: #b30000;'>Si no solicitaste este cambio, por favor ignora este correo.</p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "        <tr>" +
                "            <td align='center' style='padding: 20px; color: #888888; font-size: 12px;'>" +
                "                <p>Saludos,<br>El equipo de soporte</p>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Configuración del correo
            helper.setSubject(asunto);
            helper.setTo(destinatario);
            helper.setText(mensajeHtml, true); // true permite contenido HTML

            // Enviar el correo
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo de recuperación de contraseña", e);
        }
    }

    public void enviarCorreoCreacionCuentaAdministradorZonal(String destinatario, String nombreUsuario, String contrasenaTemporal, String enlaceCambioContrasena) {
        String asunto = "Bienvenido a Nuestro Sistema - Tu Cuenta Ha Sido Creada";

        String mensajeHtml = "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body { margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333333; }" +
                "        .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
                "        .container { max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }" +
                "        .btn { display: inline-block; padding: 12px 24px; margin: 20px 0; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; font-size: 16px; }" +
                "        .footer { text-align: center; font-size: 12px; color: #888888; margin-top: 20px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='header'>" +
                "        <h1>Bienvenido, " + nombreUsuario + "!</h1>" +
                "    </div>" +
                "    <div class='container'>" +
                "        <p>Nos complace informarte que tu cuenta ha sido creada exitosamente en nuestro sistema.</p>" +
                "        <p>Por motivos de seguridad, hemos generado una contraseña temporal para ti:</p>" +
                "        <p style='font-size: 18px; font-weight: bold;'>" + contrasenaTemporal + "</p>" +
                "        <p>Te recomendamos cambiar tu contraseña lo antes posible. Para hacerlo, haz clic en el siguiente botón:</p>" +
                "        <a href='" + enlaceCambioContrasena + "' class='btn'>Cambiar mi Contraseña</a>" +
                "        <p>Si no solicitaste la creación de esta cuenta, por favor contacta a nuestro equipo de soporte de inmediato.</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "        <p>Gracias por confiar en nosotros.<br>El equipo de soporte</p>" +
                "    </div>" +
                "</body>" +
                "</html>";


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
            throw new RuntimeException("Error al enviar el correo de creación de cuenta", e);
        }
    }

    public void enviarCorreoCambioContraseniaTemporal(String destinatario, String nombreUsuario) {
        String asunto = "Cambio de Contraseña Temporal Exitoso";

        String mensajeHtml = "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body { margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333333; }" +
                "        .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
                "        .container { max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }" +
                "        .footer { text-align: center; font-size: 12px; color: #888888; margin-top: 20px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='header'>" +
                "        <h1>¡Cambio de Contraseña Exitoso!</h1>" +
                "    </div>" +
                "    <div class='container'>" +
                "        <p>Hola <strong>" + nombreUsuario + "</strong>,</p>" +
                "        <p>Te confirmamos que tu contraseña temporal ha sido cambiada exitosamente.</p>" +
                "        <p>Ahora puedes acceder al sistema utilizando tu nueva contraseña. Por favor, asegúrate de recordarla y no compartirla con nadie para mantener la seguridad de tu cuenta.</p>" +
                "        <p>Si no realizaste este cambio o crees que alguien más lo hizo, por favor contacta inmediatamente a nuestro equipo de soporte para proteger tu cuenta.</p>" +
                "    </div>" +
                "    <div class='footer'>" +
                "        <p>Gracias por confiar en nosotros.<br>El equipo de soporte</p>" +
                "    </div>" +
                "</body>" +
                "</html>";



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
