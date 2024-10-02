package com.app.tradogt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.app.tradogt.repository.*;
import org.springframework.ui.Model;
import com.app.tradogt.entity.Usuario;

import java.util.List;


@Controller
@RequestMapping("/adminzonal")
public class AdminZonalController {

    final UsuarioRepository usuarioRepository;

    public AdminZonalController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
    public String showFechasArribo() {
        return "AdminZonal/tablaFechaArribo-AdminZonal";
    }

    @GetMapping("/reposicionProductos")
    public String showReposicionProductos() {
        return "AdminZonal/tablaReposicionProductos-AdminZonal";
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

        return "AdminZonal/gestionAgente-AdminZonal"; }

    @GetMapping("/nuevoAgente")
    public String showNuevoAgente() { return "AdminZonal/nuevoAgente-AdminZonal"; }

    @GetMapping("/verAgente")
    public String showVerAgente() { return "AdminZonal/verAgente-AdminZonal"; }



}
