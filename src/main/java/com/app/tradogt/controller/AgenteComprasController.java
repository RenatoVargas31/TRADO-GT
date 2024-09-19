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

    //TABLEROS DE ÓRDENES
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

    //Información de órdenes de usuarios asignados
    //OJO: Se crean múltiples vistas de estos detalles con el objetivo de mostrar el html
    //mas una vez se defina en el backend la lógica solo será necesario una vista que
    //varíe según corresponda el estado
    @GetMapping("/detailsOrderEnProgreso")
    public String showInfoOrder() {
        return "Agente/detallesEnProgresoProducto-Agente";
    }
    @GetMapping("/detailsOrderPendiente")
    public String showInfoOrderPending() {
        return "Agente/detallesPendientesProducto-Agente";
    }
    @GetMapping("/detailsOrderSinAsignar")
    public String showInfoOrderSinAsignar() {
        return "Agente/detallesSinAsignarProducto-Agente";
    }
    @GetMapping("/detailsOrderResuelta")
    public String showInfoOrderResuelta() {
        return "Agente/detallesResueltasProducto-Agente";
    }
    @GetMapping("/detailsOrderCancelada")
    public String showInfoOrderCancelada() {
        return "Agente/detallesCanceladoProducto-Agente";
    }


    //Tableros de USUARIOS
    @GetMapping("/allUsers")
    public String showAllUsers() {
        return "Agente/allUsersTable-Agente";
    }
    @GetMapping("/habilitadosUsers")
    public String showHabilitadosUsers() {
        return "Agente/habilitadosUsersTable-Agente";
    }
    @GetMapping("/baneadosUsers")
    public String showBaneadosUsers() {
        return "Agente/baneadosUsersTable-Agente";
    }

    //Información sobre usuarios asignados a agentes
    @GetMapping("/infoUsuario")
    public String showInfoUser(){
        return "Agente/detallesUsuarios-Agente";
    }
    @GetMapping("/infoUsuarioBaneado")
    public String showInfoUserBan(){
        return "Agente/detallesUsuariosBaneados-Agente";
    }

    //CHAT CON USUARIOS
    @GetMapping("/chat")
    public String showChat() {
        return "Agente/chatConUsuarios-Agente";
    }

    //PREGUNTAS FRECUENTES
    @GetMapping("/faq")
    public String showFaq() {
        return "Agente/faq-Agente";
    }



}
