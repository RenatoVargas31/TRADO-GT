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

    @GetMapping("/categoriaMujer")
    public String showMujerCategoria() {
        return "usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaHombre")
    public String showHombreCategoria() {
        return "usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaTecnologia")
    public String showTecnologiaCategoria() {
        return "usuario/CategoriaTecnologia-usuario";
    }
    @GetMapping("/categoriaMuebles")
    public String showMuebleCategoria() {
        return "usuario/CategoriaMuebles-usuario";
    }

    @GetMapping("/productoDetalles")
    public String showproductoDetalles() {
        return "usuario/producto-detalles";}

    @GetMapping("/carrito")
    public String showcarrito() {
        return "usuario/carrito-usuario";
    }

    @GetMapping("/checkout")
    public String showcheckout() {
        return "usuario/billing-info-usuario";
    }
    @GetMapping("/ordenCompra")
    public String showordenCompra() {
        return "usuario/orden-compra-usuario";
    }
}
