package com.app.tradogt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioFinalController {

    @GetMapping("/inicio")
    public String inicio() {
        return "Usuario/inicio-usuario";
    }

    @GetMapping("/misPedidos")
    public String misPedidos() {
        return "Usuario/listaOrdenes";
    }

    @GetMapping("/editarOrdenes")
    public String formularioPedido() {
        return "Usuario/formOrdenes";
    }

    @GetMapping("/tracking")
    public String tracking() {
        return "Usuario/trackingOrd";
    }

    @GetMapping("/foro")
    public String foro() {
        return "Usuario/foro";
    }
    @GetMapping("/foroConsultas")
    public String foroConsultas() {
        return "Usuario/consulta";
    }
    @GetMapping("/foroProblema")
    public String foroProblema() {
        return "Usuario/problema-soluciones";
    }
}
