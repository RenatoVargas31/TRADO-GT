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

    @GetMapping("/perfil")
    public String verPerfil(){return "Usuario/profile_user";}

    @GetMapping("/editarOrdenes")
    public String formularioPedido() {
        return "Usuario/formOrdenes";
    }

    @GetMapping("/tracking")
    public String tracking() {
        return "Usuario/trackingOrd";
    }

    @GetMapping("/categoriaMujer")
    public String showMujerCategoria() {
        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaHombre")
    public String showHombreCategoria() {
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaTecnologia")
    public String showTecnologiaCategoria() {
        return "Usuario/CategoriaTecnologia-usuario";
    }
    @GetMapping("/categoriaMuebles")
    public String showMuebleCategoria() {
        return "Usuario/CategoriaMuebles-usuario";
    }

    @GetMapping("/productoDetalles")
    public String showproductoDetalles() {
        return "Usuario/producto-detalles";}

    @GetMapping("/carrito")
    public String showcarrito() {
        return "Usuario/carrito-usuario";
    }

    @GetMapping("/checkout")
    public String showcheckout() {
        return "Usuario/billing-info-usuario";
    }
    @GetMapping("/ordenCompra")
    public String showordenCompra() {
        return "Usuario/orden-compra-usuario";
    }
    @GetMapping("/foro")
    public String showForo() {
        return "Usuario/foro";
    }

    @GetMapping("/foroConsultas")
    public String showForoConsultas() {
        return "Usuario/consulta";
    }
    @GetMapping("/foroProblema")
    public String showForoProblema() {
        return "Usuario/problema-soluciones";
    }

    @GetMapping("/solicitud")
    public String vistaPostulacion() {return "Usuario/postulacion";}

    @GetMapping("/registro")
    public String registroPostulacion() {return "Usuario/registroSolicitud";}
}
