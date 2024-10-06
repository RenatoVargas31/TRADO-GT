package com.app.tradogt.controller;


import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.ProductoEnZonaEnOrden;
import com.app.tradogt.entity.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.tradogt.repository.*;
import com.app.tradogt.entity.Producto;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/usuario")
public class UsuarioFinalController {

    final ProductosRepository productosRepository;
    final OrdenRepository ordenRepository;
    final UsuarioRepository usuarioRepository;
    final ProductoEnZonaEnOrdenRepository productoEnZonaEnOrdenRepository;


    public UsuarioFinalController(ProductosRepository productosRepository, OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, ProductoEnZonaEnOrdenRepository productoEnZonaEnOrdenRepository1, ProductoEnZonaEnOrdenRepository productoEnZonaEnOrdenRepository) {
        this.productosRepository = productosRepository;
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoEnZonaEnOrdenRepository = productoEnZonaEnOrdenRepository;
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "Usuario/inicio-usuario";
    }

    @GetMapping("/misPedidos")
    public String misPedidos(Model model) {
        // Imprimir los elementos de la lista
        List<Orden> misOrdenes = ordenRepository.findAllByEsCarritoAndIsDeleted(0,0);
        model.addAttribute("misOrdenes", misOrdenes);
        return "Usuario/listaOrdenes";
    }
    //Borrar una orden
    @GetMapping("/deleteOrden")
    public String deleteOrden(Model model, @RequestParam("codigo") String codigo, RedirectAttributes attr) {


        Optional<Orden> orden = ordenRepository.findByCodigo(codigo);

        if (orden.isPresent()) {
            Orden ord = orden.get();
            ord.setIsDeleted((byte) 1);
            ordenRepository.save(ord);
            String correo = ord.getUsuarioIdusuario().getCorreo();
            attr.addFlashAttribute("mensaje", "Orden eliminado: se a realizar el rembolso al " + correo);
        }else {
            attr.addFlashAttribute("error", "Orden no encontrado");
        }
        return "redirect:/usuario/misPedidos";
    }
    //Guardar la calificación del Agente de Compra
    @PostMapping("/calificarAgente")
    public String saveCalificacion(@RequestParam("ordenCodigo") String ordenCodigo,
                                   @RequestParam("valoracionAgente") int valoracionAgente,RedirectAttributes attr){

        Optional<Orden> orden = ordenRepository.findByCodigo(ordenCodigo);
        if (orden.isPresent()){
            Orden ord = orden.get();
            ord.setValoracionAgente((byte) valoracionAgente);
            ordenRepository.save(ord);
            attr.addFlashAttribute("msg", "¡Valoración guardado correctamente!");
        }
        return "redirect:/usuario/misPedidos";
    }

    @GetMapping("/perfil")
    public String verPerfil(){return "Usuario/profile_user";}

    @GetMapping("/editarOrdenes")
    public String formularioPedido() {
        return "Usuario/formOrdenes";
    }

    @GetMapping("/tracking/{id}")
    public String tracking(@PathVariable("id") int id, Model model) {

        //Lista de todos los productos de mi orden
        List<ProductoEnZonaEnOrden> listaProductosEnOrden = productoEnZonaEnOrdenRepository.findByordenIdordenId((Integer) id);

        //Buscar Usuario :'v
        Optional<Orden> orden = ordenRepository.findById(id);
        if (orden.isPresent()) {
            model.addAttribute("orden", orden.get());
            model.addAttribute("listaProductosEnOrden", listaProductosEnOrden);
        }

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
