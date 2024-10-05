package com.app.tradogt.controller;

import com.app.tradogt.entity.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.app.tradogt.repository.*;
import com.app.tradogt.entity.Usuario;
import org.springframework.ui.Model;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/adminzonal")
public class AdminZonalController {
    /*
    //Reposirotios
    final OrdenRepository ordenRepository;
    final UsuarioRepository usuarioRepository;
    final ProductosRepository productosRepository;
    final ProductoEnZonaRepository productoEnZonaRepository;

    public AdminZonalController(OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, ProductosRepository productosRepository, ProductoEnZonaRepository productoEnZonaRepository) {
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
        this.productosRepository = productosRepository;
        this.productoEnZonaRepository = productoEnZonaRepository;
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
        List<Object[]> ordenesConFechas = ordenRepository.findOrdersByZone();
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
    /*@PostMapping("/editarEstadoRepo")
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
        List<Object[]> usuarioDetails = usuarioRepository.getUsuarioOrderProductDetails();
        model.addAttribute("usuarioDetails", usuarioDetails);

        List<Object[]> agentes = usuarioRepository.getAgenteDetailsNative();
        model.addAttribute("agentes", agentes);

        List<Usuario> usuarios = usuarioRepository.findAllById();
        model.addAttribute("usuarios", usuarios);



        return "AdminZonal/gestionAgente-AdminZonal"; }

    @GetMapping("/nuevoAgente")
    public String showNuevoAgente() { return "AdminZonal/nuevoAgente-AdminZonal"; }


    @GetMapping("/verAgente/{id}")
    public String showVerAgente(@PathVariable("id") Integer usuarioId, Model model) {
        List<Object[]> agenteDetails = usuarioRepository.getAgenteDetailsById(usuarioId);

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
        }
        return "AdminZonal/verAgente-AdminZonal";
    }


*/
}
