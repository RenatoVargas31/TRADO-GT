package com.app.tradogt.controller;

import com.app.tradogt.entity.Distrito;
import com.app.tradogt.entity.Rol;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.repository.DistritoRepository;
import com.app.tradogt.repository.RolRepository;
import com.app.tradogt.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private RolRepository rolRepository;

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
                    case "Agente de Compra" -> response.sendRedirect("/agente/inicio");
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
                                   RedirectAttributes redirectAttributes) {

        // Verify if the entered district exists
        Optional<Distrito> distritoOpt = distritoRepository.findByNombre(distritoNombre);
        if (distritoOpt.isEmpty()) {
            result.rejectValue("distritoIddistrito", "error.usuario", "El distrito ingresado no es valido.");
            return "CreateAcc"; // Return to the form if the district is not valid
        }

        // Assign the district to the user
        usuario.setDistritoIddistrito(distritoOpt.get());

        // Check for form errors
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("denegado", "Usuario registrado sin exito.");
            return "CreateAcc";
        }

        // Save the user in the database
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario registrado con exito.");
        return "CreateAcc-confirm";
    }
}