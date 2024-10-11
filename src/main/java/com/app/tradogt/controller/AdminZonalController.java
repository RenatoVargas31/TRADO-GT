package com.app.tradogt.controller;

import com.app.tradogt.dto.AgenteInfoZon;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Producto;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

        //Añadir código
        List<Object[]> productos = productosRepository.findOrdersByRepo();
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
    @GetMapping("/perfil")
    public String showPerfil() {
        return "AdminZonal/profile";
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
        /*List<Object[]> agenteDetails = usuarioRepository.getAgenteDetailsById(usuarioId);

        if (!agenteDetails.isEmpty()) {
            Object[] details = agenteDetails.get(0);
            model.addAttribute("AgenteID", details[0]);
            model.addAttribute("AgenteNombre", details[1]);
            model.addAttribute("AgenteApellido", details[2]);
            model.addAttribute("AgenteDni", details[3]);
            model.addAttribute("AgenteCorreo", details[4]);
            model.addAttribute("AgenteTelefono", details[5]);
            model.addAttribute("ZonaNombre", details[6]);
            model.addAttribute("DistritoNombre", details[7]);
            model.addAttribute("AgenteDireccion", details[8]);
            model.addAttribute("CodigoDespachador", details[9]);
            model.addAttribute("AgenteRUC", details[10]);
            model.addAttribute("AgenteRazonSocial", details[11]);
        }*/
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
