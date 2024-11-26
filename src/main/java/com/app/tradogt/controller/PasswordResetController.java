package com.app.tradogt.controller;

import com.app.tradogt.entity.PasswordResetToken;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.repository.PasswordResetTokenRepository;
import com.app.tradogt.repository.UsuarioRepository;
import com.app.tradogt.services.NotificationCorreoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class PasswordResetController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificationCorreoService notificationCorreoService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;



    @PostMapping("/request-password-reset")
    public String requestPasswordReset(@RequestParam String email, RedirectAttributes redirectAttributes) {

        try{
            //Enviamos el correo
            Optional<Usuario> user = Optional.ofNullable(usuarioRepository.findByCorreo(email));

            if (!user.isPresent()) {
                throw new IllegalArgumentException("El correo no está registrado.");
            }

            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setEmail(email);
            resetToken.setToken(token);
            resetToken.setExpirationDate(Instant.now().plus(30, ChronoUnit.MINUTES));
            resetToken.setCreatedAt(Instant.now());
            tokenRepository.save(resetToken);

            // Enviar el token por correo
            notificationCorreoService.enviarCorreoRecuperacion(email, user.get().getNombre(), token);

            redirectAttributes.addFlashAttribute("success", "¡El correo de recuperación se ha enviado exitosamente!");
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se encontró una cuenta con ese correo electrónico.");
        }

        return "redirect:/recuperarPass";
    }
}
