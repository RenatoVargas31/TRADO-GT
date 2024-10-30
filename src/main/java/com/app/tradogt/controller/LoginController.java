package com.app.tradogt.controller;

import com.app.tradogt.daos.DniDao;
import com.app.tradogt.entity.*;
import com.app.tradogt.repository.*;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private DniDao dniDao;
    final EstadoOrdenRepository estadoOrdenRepository;
    final OrdenRepository ordenRepository;

    public LoginController(EstadoOrdenRepository estadoOrdenRepository, OrdenRepository ordenRepository) {
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.ordenRepository = ordenRepository;
    }

    //Actualiza los estados de las ordenes
    public void updateOrderStatus() {
        //Obtener la fecha actual
        LocalDate today = LocalDate.now();

        Optional<EstadoOrden> estadoactual = estadoOrdenRepository.findById(3);
        Optional<EstadoOrden> arriboAlPais = estadoOrdenRepository.findById(4);
        Optional<EstadoOrden> aduanas = estadoOrdenRepository.findById(5);
        Optional<EstadoOrden> ruta = estadoOrdenRepository.findById(6);
        Optional<EstadoOrden> recibido = estadoOrdenRepository.findById(7);

        List<Orden> ordensInProcess = ordenRepository.findByEstadoordenIdestadoorden(estadoactual);

        for (Orden orden : ordensInProcess) {
            System.out.println("Orden ID: " + orden.getId());

            // Cambiar el estado de acuerdo a la fecha actual
            if (orden.getFechaArribo() != null && orden.getFechaArribo().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(arriboAlPais.get());
            } else if (orden.getFechaEnAduanas() != null && orden.getFechaEnAduanas().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(aduanas.get());
            } else if (orden.getFechaEnRuta() != null && orden.getFechaEnRuta().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(ruta.get());
            } else if (orden.getFechaRecibido() != null && orden.getFechaRecibido().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(recibido.get());
            }else{
                orden.setEstadoordenIdestadoorden(orden.getEstadoordenIdestadoorden());
            }

            // Guardar la orden actualizada
            ordenRepository.save(orden);
        }
    }

    @GetMapping("/loginForm")
    public String loginForm(HttpServletRequest request, HttpServletResponse response, Model model,
                            @RequestParam(value = "error", required = false) String error) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //Actualiza los estados de los pedidos
        updateOrderStatus();

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
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
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

        // Guardar el usuario en la base de datos
        usuario.setDistritoIddistrito(distritoOpt.get());
        usuarioRepository.save(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario registrado con éxito.");
        return "CreateAcc-confirm";
    }
}