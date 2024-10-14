package com.app.tradogt.controller;

import com.app.tradogt.dto.AgenteInfoZon;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Producto;
import com.app.tradogt.entity.ProductoEnZona;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.app.tradogt.repository.*;
import com.app.tradogt.entity.Usuario;
import org.springframework.ui.Model;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/adminzonal")
public class AdminZonalController {

    //Reposirotios
    final OrdenRepository ordenRepository;
    final UsuarioRepository usuarioRepository;
    final ProductosRepository productosRepository;
    final ProductoEnZonaRepository productoEnZonaRepository;
    private final DistritoRepository distritoRepository;
    private final RolRepository rolRepository;
    private final ZonaRepository zonaRepository;

    public AdminZonalController(OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, ProductosRepository productosRepository, ProductoEnZonaRepository productoEnZonaRepository, DistritoRepository distritoRepository, RolRepository rolRepository, ZonaRepository zonaRepository) {
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
        this.productosRepository = productosRepository;
        this.productoEnZonaRepository = productoEnZonaRepository;
        this.distritoRepository = distritoRepository;
        this.rolRepository = rolRepository;
        this.zonaRepository = zonaRepository;
    }

    private int getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;

        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername();
        } else {
            correoUsuario = principal.toString();
        }

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
        return usuario.getId();
    }

    @GetMapping("/vacio")
    public String home() {
        return "AdminZonal/starter-AdminZonal";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "AdminZonal/dashboard-AdminZonal";
    }

    @GetMapping("/fechasArribo")
    public String showFechasArribo(Model model) {

        //Añadir codigo
        List<Orden> ordenesConFechas = ordenRepository.findAll();
        model.addAttribute("ordenesConFechas", ordenesConFechas);
        return "AdminZonal/tablaFechaArribo-AdminZonal";
    }

    @GetMapping("/reposicionProductos")
    public String showReposicionProductos(Model model) {

        List<ProductoEnZona> productos = productoEnZonaRepository.findAllByZonaIdzonaAndIsDeleted(zonaRepository.findById(1).get(), Byte.parseByte("0"));
        model.addAttribute("productos", productos);
        return "AdminZonal/tablaReposicionProductos-AdminZonal";
    }

    //Editar el estado de reposición
    @PostMapping("/editarEstadoRepo")
    public String editarEstadoRepo(Producto producto) {

        //Actualizar el estado

        return "redirect:/adminzonal/reposicionProductos";
    }

    @GetMapping("/faq")
    public String showFaq() {
        return "AdminZonal/faq";
    }

    @PostMapping("/subirFoto")
    public String viewSubirFoto(@RequestParam("foto") MultipartFile file, Model model) throws IOException {
        System.out.println("Entré al controler");

        // Ruta dinámica donde se guardarán las imágenes (fuera de static)
        String uploadDir = "uploads/fotosUsuarios/";

        // Guardar el archivo en la ruta definida
        byte[] bytes = file.getBytes();
        Path path = Paths.get(uploadDir + file.getOriginalFilename());
        Files.write(path, bytes);
        System.out.println("Guardé la foto en: " + path);

        // Obtener el usuario autenticado desde el modelo
        Usuario usuario = (Usuario) model.getAttribute("usuarioAutenticado");
        assert usuario != null;

        // Actualizar el nombre de la foto en la base de datos
        usuario.setFoto(file.getOriginalFilename());
        System.out.println("Seteé la foto como: " + file.getOriginalFilename());
        usuarioRepository.save(usuario);
        System.out.println("Guardé el usuario");

        // Redirigir al perfil
        return "redirect:/adminzonal/perfil";
    }




    @GetMapping("/perfil")
    public String showPerfil (@ModelAttribute("user") Usuario user,  Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(getAuthenticatedUserId());
        if (usuario.isPresent()) {
            model.addAttribute("user", usuario.get());
            return "AdminZonal/profile";
        } else {
            return "redirect:/logout";
        }

    }

    @PostMapping("/editarPerfil")
    public String editarPerfil(@ModelAttribute("user") Usuario user, Model model) {
        Usuario usuario = usuarioRepository.findById(getAuthenticatedUserId()).get();
        usuario.setDireccion(user.getDireccion());
        usuario.setTelefono(user.getTelefono());
        usuarioRepository.save(usuario);
        return "redirect:/adminzonal/perfil";
    }

    @GetMapping("/contraseña")
    public String showPassword() { return "AdminZonal/password"; }

    @GetMapping("/gestionAgente")
    public String showGestionAgente(Model model) {
        /*List<Object[]> usuarioDetails = usuarioRepository.getUsuarioOrderProductDetails();
        model.addAttribute("usuarioDetails", usuarioDetails);*/

        /*List<Usuario> agentes = usuarioRepository.findAllByAdmzonalIdusuario_IdAndIsActivated(2, Byte.parseByte("1"));
        model.addAttribute("agentes", agentes);*/

        List<AgenteInfoZon> agentes = usuarioRepository.getAgentesbyZonal(2);
        model.addAttribute("agentes", agentes);

        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);

        List<Orden> ordenes = ordenRepository.findAllByAgentcompraIdusuario(usuarioRepository.findByIdUsuario(4));
        model.addAttribute("ordenes", ordenes);


        return "AdminZonal/gestionAgente-AdminZonal"; }

    @GetMapping("/nuevoAgente")
    public String showNuevoAgente(Model model, @ModelAttribute("agente") Usuario usuario) {
        model.addAttribute("listaDistritos", distritoRepository.findByZonaIdzonaId(1));
        Optional<Usuario> AgentePrueba = usuarioRepository.findById(1);
        model.addAttribute("agenteA", AgentePrueba.get());
        return "AdminZonal/nuevoAgente-AdminZonal";
    }

    @PostMapping("/saveAgente")
    public String saveAgente(@ModelAttribute("agente") Usuario usuario, BindingResult bindingResult, RedirectAttributes attr) {
        if(bindingResult.hasErrors()){
            return "nuevoAgente-AdminZonal";
        } else {
            attr.addFlashAttribute("msg", "Agente " + (usuario.getId() == null ? "creado exitosamente" : "actualizado exitosamente"));
            usuario.setIsActivated(Byte.parseByte("1"));
            usuario.setIsPostulated(Byte.parseByte("1"));
            usuario.setRolIdrol(rolRepository.findById(4).get());
            usuario.setZonaIdzona(zonaRepository.findById(1).get());
            usuarioRepository.save(usuario);
            return "redirect:/adminzonal/gestionAgente";
        }
    }


    @GetMapping("/verAgente/{id}")
    public String showVerAgente(@PathVariable("id") Integer usuarioId, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        model.addAttribute(usuario.get());
        return "AdminZonal/verAgente-AdminZonal";
    }

    @PostMapping("/editarFecha")
    public String actualizarFechaArribo(@RequestParam("orderId") String orderId,
                                        @RequestParam("fechaArribo") LocalDate fechaArribo) {
        // Lógica para actualizar la fecha de arribo en la base de datos
        Orden orden = ordenRepository.findByCodigo(orderId).get();
        orden.setFechaArribo(fechaArribo);
        ordenRepository.save(orden);

        return "redirect:/adminzonal/fechasArribo";  // Redirigir a la vista deseada
    }



}
