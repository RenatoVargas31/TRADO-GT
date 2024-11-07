package com.app.tradogt.controller;

import com.app.tradogt.daos.DniDao;
import com.app.tradogt.entity.*;
import com.app.tradogt.repository.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private RolRepository rolRepository;

    //Para el envío de correos
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    private DniDao dniDao;
    final EstadoOrdenRepository estadoOrdenRepository;
    final OrdenRepository ordenRepository;

    public LoginController(EstadoOrdenRepository estadoOrdenRepository, OrdenRepository ordenRepository) {
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.ordenRepository = ordenRepository;
    }
    @GetMapping("/")
    public String white(HttpServletRequest request, HttpServletResponse response, Model model,
                        @RequestParam(value = "error", required = false) String error) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario ya está autenticado
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String rol = "";
            for (GrantedAuthority role : auth.getAuthorities()) {
                rol = role.getAuthority();
                break;
            }
            try {
                switch (rol) {
                    case "SuperAdmin" -> response.sendRedirect("/superadmin/inicio");
                    case "Administrador Zonal" -> response.sendRedirect("/adminzonal/dashboard");
                    case "Agente de Compra" -> response.sendRedirect("/agente/allOrders");
                    default -> response.sendRedirect("/usuario/inicio");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Verificar si hubo un error de credenciales
        if (error != null) {
            model.addAttribute("error", "Credenciales incorrectas. Por favor, inténtalo de nuevo.");
        }
        return "loguin";
    }

    @GetMapping("/loginForm")
    public String loginForm(HttpServletRequest request, HttpServletResponse response, Model model,
                            @RequestParam(value = "error", required = false) String error) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario ya está autenticado
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String rol = "";
            for (GrantedAuthority role : auth.getAuthorities()) {
                rol = role.getAuthority();
                break;
            }
            try {
                switch (rol) {
                    case "SuperAdmin" -> response.sendRedirect("/superadmin/inicio");
                    case "Administrador Zonal" -> response.sendRedirect("/adminzonal/dashboard");
                    case "Agente de Compra" -> response.sendRedirect("/agente/allOrders");
                    default -> response.sendRedirect("/usuario/inicio");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Verificar si hubo un error de credenciales
        if (error != null) {
            model.addAttribute("error", "Credenciales incorrectas. Por favor, inténtalo de nuevo.");
        }
        return "loguin";
    }

    @GetMapping("/crearCuenta")
    public String crearCuenta(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("distritos", distritoRepository.findAll());
        return "CreateAcc";
    }


    @PostMapping("/crearCuenta")
    public String registrarUsuario(@Valid @ModelAttribute Usuario usuario,
                                   BindingResult result,
                                   @RequestParam("distrito") String distritoNombre,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        
        // Validar que las contraseñas coincidan
        if (!usuario.getContrasena().equals(usuario.getConfirmarContrasena())) {
            result.rejectValue("confirmarContrasena", "error.usuario", "Las contraseñas no coinciden.");
            return "CreateAcc";
        }

        // Volver a cargar la lista de distritos
        List<Distrito> distritos = distritoRepository.findAll();
        model.addAttribute("distritos", distritos);

        // Verificar si el distrito existe
        Optional<Distrito> distritoOpt = distritoRepository.findByNombre(distritoNombre);

        if (distritoOpt.isEmpty()) {
            result.rejectValue("distritoIddistrito", "error.usuario", "El distrito ingresado no es valido.");
            return "CreateAcc"; // Volver al formulario si el distrito no es válido
        }

        // Validate DNI
        if (!usuario.getDni().matches("\\d{8}")) {
            result.rejectValue("dni", "error.usuario", "El DNI debe ser un valor único de 8 dígitos.");
            return "CreateAcc";
        }
        if (usuarioRepository.existsByDni(usuario.getDni())) {
            result.rejectValue("dni", "error.usuario", "El DNI ya está registrado.");
            return "CreateAcc";
        }

        // Validate name and surname
        if (!usuario.getNombre().matches("[a-zA-Z\\s]+")) {
            result.rejectValue("nombre", "error.usuario", "El nombre no debe contener números.");
            return "CreateAcc";
        }
        if (!usuario.getApellido().matches("[a-zA-Z\\s]+")) {
            result.rejectValue("apellido", "error.usuario", "El apellido no debe contener números.");
            return "CreateAcc";
        }

        // Validate email
        if (!usuario.getCorreo().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            result.rejectValue("correo", "error.usuario", "El correo debe ser único y contener un @.");
            return "CreateAcc";
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            result.rejectValue("correo", "error.usuario", "El correo ya está registrado.");
            return "CreateAcc";
        }

        // Validate no empty fields
        if (usuario.getDni().trim().isEmpty() || usuario.getNombre().trim().isEmpty() ||
                usuario.getApellido().trim().isEmpty() || usuario.getCorreo().trim().isEmpty() ||
                usuario.getDireccion().trim().isEmpty()) {
            result.rejectValue("general", "error.usuario", "Ningún campo debe estar vacío o contener solo espacios.");
            return "CreateAcc";
        }

        // Si hay errores, recargar el formulario con la lista de distritos
        if (result.hasErrors()) {
            return "CreateAcc";
        }

        // Cifrar la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        //Cambiar campo isActived a 1
        usuario.setIsActivated((byte) 1);
        // Guardar el usuario en la base de datos
        usuario.setDistritoIddistrito(distritoOpt.get());

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute("success", "Usuario registrado con éxito.");
        // Enviar el correo de bienvenida
        String mensajeBienvenida = "¡Hola " + usuario.getNombre() + "!\n\n" +
                "Gracias por registrarte en Trado. Ahora puedes iniciar sesión en nuestra plataforma para explorar todas nuestras ofertas y productos.\n\n" +
                "Saludos,\nEquipo de Trado";

        sendMessage(usuario.getCorreo(), mensajeBienvenida);
        return "redirect:/CreateAcc-confirm";
    }

    public void sendMessage(String email, String messageEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("¡Bienvenido a Trado!");
            helper.setTo(email);

            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang='es'>" +
                    "<head>" +
                    "    <meta charset='UTF-8'>" +
                    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "</head>" +
                    "<body style='margin:0; padding:0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                    "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                    "        <tr>" +
                    "            <td align='center' bgcolor='#800080' style='padding: 40px 0; color: #ffffff;'>" +
                    "                <h1 style='margin: 0; font-size: 24px;'>¡Bienvenido a Trado!</h1>" +
                    "                <p style='font-size: 16px;'>Nos alegra que te unas a nosotros</p>" +
                    "            </td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <td align='center' style='padding: 20px;'>" +
                    "                <table width='600' border='0' cellspacing='0' cellpadding='0' style='background-color: #ffffff; border-radius: 8px; padding: 40px;'>" +
                    "                    <tr>" +
                    "                        <td align='center' style='padding: 20px; color: #333333;'>" +
                    "                            <h2 style='font-size: 20px; color: #800080;'>Empieza tu viaje con nosotros</h2>" +
                    "                            <p style='font-size: 16px; line-height: 1.5;'>" + messageEmail + "</p>" + // Inserta el contenido aquí
                    "                            <a href='#' style='display: inline-block; margin-top: 20px; padding: 12px 24px; background-color: #800080; color: #ffffff; text-decoration: none; border-radius: 5px; font-size: 16px;'>Iniciar Sesión</a>" +
                    "                        </td>" +
                    "                    </tr>" +
                    "                </table>" +
                    "            </td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <td align='center' style='padding: 20px; color: #888888; font-size: 12px;'>" +
                    "                <p>&copy; 2024 Trado. Todos los derechos reservados.</p>" +
                    "            </td>" +
                    "        </tr>" +
                    "    </table>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            helper.setFrom(sender);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo de bienvenida", e);
        }
    }


}