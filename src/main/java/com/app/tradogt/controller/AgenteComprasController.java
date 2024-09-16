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
    //Tableros de ordenes
    @GetMapping("/allOrders")
    public String showAllOrders() {
        return "Agente/allOrdersTable-Agente";
    }
    @GetMapping("/sinAsignarOrders")
    public String showSinAsignarOrders() {
        return "Agente/sinAsignarOrdersTable-Agente";
    }
    @GetMapping("/pendingOrders")
    public String showPendingOrders() {
        return "Agente/pendingOrdersTable-Agente";
    }
    @GetMapping("/enProcesoOrders")
    public String showEnProcesoOrders() {
        return "Agente/procesoOrdersTable-Agente";
    }
    @GetMapping("/solveOrders")
    public String showSolveOrders() {
        return "Agente/solveOrdersTable-Agente";
    }
    @GetMapping("/perfil")
    public String showPerfil() {
        return "Agente/profile-Agente";
    }

    //PREGUNTAS FRECUENTES 
    @GetMapping("/faq")
    public String showFaq() {
        return "Agente/faq-Agente";
    }



}
