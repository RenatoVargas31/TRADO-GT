package com.app.tradogt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.tradogt.repository.*;
import com.app.tradogt.entity.Producto;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


@Controller
@RequestMapping("/usuario")
public class UsuarioFinalController {

    final ProductosRepository productosRepository;

    public UsuarioFinalController(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "Usuario/inicio-usuario";
    }

    @GetMapping("/misPedidos")
    public String misPedidos(Model model) {
        /*List<Producto> productos = productosRepository.findAll();
        model.addAttribute("productos", productos); */
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

    @GetMapping("/editOrden")
    public String editOrden() {
        return "Usuario/trackingOrdEdit";
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
    @GetMapping("/reseñas")
    public String showResenhas() {
        return "Usuario/reseñas-usuario";
    }

    @GetMapping("/comunidad")
    public String showComunidad() {
        return "Usuario/comunidad-usuario";
    }
    @GetMapping("/foroProblema")
    public String showForoProblema() {
        return "Usuario/problema-soluciones";
    }
    @GetMapping("/verPublicacion")
    public String showDetalleConsultas() {
        return "Usuario/verPublicacion-usuario";
    }
    @GetMapping("/detalleProblema")
    public String showDetalleProblema() {
        return "Usuario/viewProblema";
    }
    @GetMapping("/verReseña")
    public String showDetalleForo() {
        return "Usuario/verReseña-usuario";
    }

    @GetMapping("/solicitud")
    public String vistaPostulacion() {return "Usuario/postulacion";}

    @GetMapping("/registro")
    public String registroPostulacion() {return "Usuario/registroSolicitud";}
    @GetMapping("/contraseña")
    public  String showpassword(){
        return "Usuario/password-usuario";
    }


    //Preguntas Soporte técnico
    @GetMapping("/soporte")
    public String showSoporte(){
        return "Usuario/soporte-usuario";
    }
    @GetMapping("/nuevaPublicación")
    public String nuevaPublicacion(){

        return "Usuario/nuevaPublicacion-usuario";
    }
    @GetMapping("/nuevaReseña")
    public String nuevaResenha(){

        return "Usuario/nuevaReseña-usuario";
    }
}
