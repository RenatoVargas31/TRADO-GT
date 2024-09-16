package com.app.tradogt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/agente")
public class AgenteComprasController {

    @GetMapping("/inicio")
    public String showInicio() {
        return "Agente/inicio-Agente";
    }

    @GetMapping("/contraseña")
    public String changePass(){
        return "Agente/changePass-Agente";
    }

    @GetMapping("/allOrders")
    public String showAllOrders() {
        return "Agente/allOrdersTable-Agente";
    }

    @GetMapping("/perfil")
    public String showPerfil() {
        return "Agente/profile-Agente";
    }

    @GetMapping("/faq")
    public String showFaq() {
        return "Agente/faq-Agente";
    }

}
