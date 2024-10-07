package com.app.tradogt.controller;


import com.app.tradogt.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.tradogt.repository.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/usuario")
public class UsuarioFinalController {

    final ProductosRepository productosRepository;
    final OrdenRepository ordenRepository;
    final UsuarioRepository usuarioRepository;
    //final ProductoEnZonaEnOrdenRepository productoEnZonaEnOrdenRepository;
    final ProductoEnZonaRepository productoEnZonaRepository;
    final PublicacionRepository publicacionRepository;
    final ComentarioRepository comentarioRepository;
    final SubCategoriaRepository subCategoriaRepository;
    final CarritoRepository carritoRepository;


    public UsuarioFinalController(ProductosRepository productosRepository, OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, ProductoEnZonaRepository productoEnZonaRepository, PublicacionRepository publicacionRepository, ComentarioRepository comentarioRepository, SubCategoriaRepository subCategoriaRepository, CarritoRepository carritoRepository) {
        this.productosRepository = productosRepository;
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
        //this.productoEnZonaEnOrdenRepository = productoEnZonaEnOrdenRepository;
        this.productoEnZonaRepository = productoEnZonaRepository;
        this.publicacionRepository = publicacionRepository;
        this.comentarioRepository = comentarioRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.carritoRepository = carritoRepository;
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "Usuario/inicio-usuario";
    }
    @GetMapping("/misPedidos")
    public String misPedidos(Model model) {
        // Imprimir los elementos de la lista
        List<Orden> misOrdenes = ordenRepository.findAllByIsDeleted(0);
        model.addAttribute("misOrdenes", misOrdenes);
        return "Usuario/listaOrdenes";
    }

    //Borrar una orden
    @GetMapping("/deleteOrden")
    public String deleteOrden(Model model, @RequestParam("codigo") String codigo, RedirectAttributes attr) {


        Optional<Orden> orden = ordenRepository.findByCodigo(codigo);

        if (orden.isPresent()) {

            //Borrar orden de la lista
            Orden ord = orden.get();
            ord.setIsDeleted((byte) 1);
            ordenRepository.save(ord);

            //Mostrar el mensaje de reembolso
            List<Carrito> miCarrito = carritoRepository.findByOrdenIdorden(ord);
            // Obtener el primer elemento de la lista
            if (!miCarrito.isEmpty()) {
                Carrito item = miCarrito.get(0); // O usar .stream().findFirst().get()
                String correoUsuario= item.getUsuarioIdusuario().getCorreo();
                attr.addFlashAttribute("mensaje", "Orden eliminado: se a realizar el rembolso al correo " + correoUsuario);
            }else{
                attr.addFlashAttribute("error", "Orden no encontrado");
            }
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
            attr.addFlashAttribute("msg", "¡Valoración guardado correctamente! Muchas gracias por su participacion");
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
        Optional<Orden> orden = ordenRepository.findById(id);
        if (orden.isPresent()){
            Orden ord = orden.get();
            //Listar mis productos
            List<Carrito> miCarrito = carritoRepository.findByOrdenIdorden(ord);
            if (!miCarrito.isEmpty()){
                //Selecionamos al usuario de la orden
                Carrito item = miCarrito.get(0);
                //Ver el costo por producto
                // Inicializa el total del costo
                BigDecimal totalCosto = BigDecimal.ZERO;
                BigDecimal totalSubTotal = BigDecimal.ZERO;

// Iterar sobre la lista para calcular el total del subtotal
                for (Carrito producto : miCarrito) {
                    // Precio unitario
                    BigDecimal costoUnidad = producto.getProductoEnZona().getProductoIdproducto().getPrecio();
                    // Unidad por producto
                    int cantidad = producto.getCantidad();
                    // Costo sub total para cada producto
                    BigDecimal subTotal = costoUnidad.multiply(new BigDecimal(cantidad));
                    // Añadir el sub total al total del costo
                    totalCosto = totalCosto.add(subTotal);
                    totalSubTotal = totalSubTotal.add(subTotal);
                }

// Setear el subtotal en la orden
                ord.setSubTotal(totalSubTotal);
                ordenRepository.save(ord);

// Costo de envio
                BigDecimal costoEnvio = item.getProductoEnZona().getCostoEnvio();
// Añadir el costo de envío al total del costo
                totalCosto = totalCosto.add(costoEnvio);

// Imprimir el total del costo
                System.out.println("Total del costo: " + totalCosto);
                System.out.println("Subtotal de la orden: " + ord.getSubTotal());
                ord.setCostoTotal(totalCosto);
                ordenRepository.save(ord);


                model.addAttribute("item", item);
                model.addAttribute("miCarrito", miCarrito);
                model.addAttribute("orden", ord);
            }
        } else {
            System.out.println("Orden no encontrada");
        }

        return "Usuario/trackingOrd";
    }

    @GetMapping("/editOrden/{id}")
    public String editOrden(@PathVariable("id") int id, Model model) {

        //Lista de todos los productos de mi orden
        Optional<Orden> orden = ordenRepository.findById(id);
        if (orden.isPresent()){
            Orden ord = orden.get();
            //Listar mis productos
            List<Carrito> miCarrito = carritoRepository.findByOrdenIdorden(ord);
            if (!miCarrito.isEmpty()){
                //Selecionamos al usuario de la orden
                Carrito item = miCarrito.get(0);
                //Ver el costo por producto

                // Inicializa el total del costo
                BigDecimal totalCosto = BigDecimal.ZERO;
                BigDecimal totalSubTotal = BigDecimal.ZERO;

// Iterar sobre la lista para calcular el total del subtotal
                for (Carrito producto : miCarrito) {
                    // Precio unitario
                    BigDecimal costoUnidad = producto.getProductoEnZona().getProductoIdproducto().getPrecio();
                    // Unidad por producto
                    int cantidad = producto.getCantidad();
                    // Costo sub total para cada producto
                    BigDecimal subTotal = costoUnidad.multiply(new BigDecimal(cantidad));
                    // Añadir el sub total al total del costo
                    totalCosto = totalCosto.add(subTotal);
                    totalSubTotal = totalSubTotal.add(subTotal);
                }

// Setear el subtotal en la orden
                ord.setSubTotal(totalSubTotal);

// Costo de envio
                BigDecimal costoEnvio = item.getProductoEnZona().getCostoEnvio();
// Añadir el costo de envío al total del costo
                totalCosto = totalCosto.add(costoEnvio);

// Imprimir el total del costo
                System.out.println("Total del costo: " + totalCosto);
                System.out.println("Subtotal de la orden: " + ord.getSubTotal());



                ord.setCostoTotal(totalCosto);
                model.addAttribute("item", item);
                model.addAttribute("miCarrito", miCarrito);
                model.addAttribute("orden", ord);
            }
        } else {
            System.out.println("Orden no encontrada");
        }
        return "Usuario/trackingOrdEdit";
    }



    @GetMapping("/productoDetalles")
    public String showproductoDetalles() {
        return "Usuario/producto-detalles";}

    @GetMapping("/carrito")
    public String showCarrito( Model model) {

        Integer iduser = 17;
        Usuario usuario = usuarioRepository.findById(iduser).get();
        model.addAttribute("usuario", usuario);
        // Lista de productos en el carrito
        List<Carrito> miCarrito = carritoRepository.findByOrdenIdordenAndUsuarioIdusuario(null,usuario );

        if (!miCarrito.isEmpty()) {
            // Inicializa el costo por producto y el costo total
            BigDecimal totalSubTotal = BigDecimal.ZERO;
            BigDecimal totalCosto = BigDecimal.ZERO;

            for (Carrito producto : miCarrito) {
                // Precio Unitario
                BigDecimal costoUnidad = producto.getProductoEnZona().getProductoIdproducto().getPrecio();
                // Cantidad por producto
                int cantidad = producto.getCantidad();
                // Obtiene el subtotal para cada producto
                BigDecimal subTotal = costoUnidad.multiply(new BigDecimal(cantidad));
                totalSubTotal = totalSubTotal.add(subTotal);
                // Establece el subtotal para cada producto
                producto.setCosto(subTotal);
                // Actualiza el costo total
                totalCosto = totalCosto.add(subTotal);
                producto.setCostoTotal(totalCosto);
                // Guarda los cambios en la base de datos
                carritoRepository.save(producto);
            }
            Carrito item = miCarrito.get(0); // Primer elemento
            ProductoEnZona productoEnZona = item.getProductoEnZona();
            BigDecimal costoEnvio = productoEnZona.getCostoEnvio();
            //Costo de envio
            model.addAttribute("costoEnvio", costoEnvio);

            // Costo total del último elemento
            BigDecimal ultimoCostoTotal = miCarrito.get(miCarrito.size() - 1).getCostoTotal();
            System.out.println("Costo: " + ultimoCostoTotal);
            model.addAttribute("costoTotal", ultimoCostoTotal);
        }
        model.addAttribute("carrito", miCarrito);
        System.out.println("Carrito: " + miCarrito);
        return "Usuario/carrito-usuario";
    }

    //Eliminar un producto  de la lista
    @PostMapping("/eliminarProducto")
    public String eliminarProducto(@RequestParam("productoId") Integer productoId,
                                   @RequestParam("usuarioId") Integer usuarioId,
                                   RedirectAttributes redirectAttributes) {

        //Buscar el usuario
        Optional<Usuario> myUser = usuarioRepository.findById(usuarioId);
        //Obtener el id producto
        Optional<Producto> myProduct = productosRepository.findById(productoId);
        //ID usuario
        int idUsuer = myUser.get().getId();
        //Id producto
        int idProduct = myProduct.get().getId();
        //Id Zona
        int idZone = myUser.get().getZonaIdzona().getId();
        // Encuentra el producto en el carrito por el ID del producto y el ID del usuario
        //List<Carrito> productosEnCarrito = carritoRepository.findByusuarioIdusuario(usuarioId);
        // Elimina el producto del carrito
        /*if (!productosEnCarrito.isEmpty()) {

            //carritoRepository.deleteAll(productosEnCarrito);
            Carrito item = productosEnCarrito.get(0);
            item.setIsDelete(true);
            carritoRepository.save(item);
        }*/

        // Añade un mensaje de éxito
        redirectAttributes.addFlashAttribute("message", "Producto eliminado exitosamente");

        return "redirect:/usuario/carrito";
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
    public String showComunidad(Model model) {
        List<Object[]> publicaciones = publicacionRepository.findPublicacionesUsuariosNoBaneados();
        model.addAttribute("publicaciones", publicaciones);
        return "Usuario/comunidad-usuario";
    }
    @GetMapping("/foroProblema")
    public String showForoProblema() {
        return "Usuario/problema-soluciones";
    }

    @GetMapping("/verPublicacion/{id}")
    public String showDetalleConsultas(@PathVariable("id") Integer id, Model model) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));
        List<Comentario> comentarios = comentarioRepository.findByPublicacionId(id);
        model.addAttribute("publicacion", publicacion);
        model.addAttribute("comentarios", comentarios);
        return "Usuario/verPublicacion-usuario";
    }

    @PostMapping("/agregarComentario")
    public String agregarComentario(@RequestParam("cuerpo") String cuerpo,
                                    @RequestParam("publicacionId") Integer publicacionId,
                                    Model model) {
        // Buscar la publicación por su ID
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        // Crear un nuevo comentario
        Comentario comentario = new Comentario();
        comentario.setCuerpo(cuerpo);
        comentario.setFechaCreacion(LocalDate.now());  // Fecha actual
        comentario.setPublicacionIdpublicacion(publicacion);

        // Simular el ID del usuario. Actualmente estamos trabajando con el ID 17
        Usuario usuario = usuarioRepository.findById(17)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        comentario.setUsuarioIdusuario(usuario);

        // Guardar el comentario en la base de datos
        comentarioRepository.save(comentario);

        // Redirigir nuevamente a la publicación para ver el comentario agregado
        return "redirect:/usuario/verPublicacion/" + publicacionId;
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
    public String nuevaPublicacion(Model model){
        model.addAttribute("publicacion", new Publicacion());
        return "Usuario/nuevaPublicacion-usuario";
    }

    @PostMapping("/nuevaPublicacion")
    public String crearPublicacion(@ModelAttribute("publicacion") Publicacion publicacion, BindingResult result, Model model) {
        // Suponiendo que estamos usando el idUsuario=17 temporalmente
        Usuario usuario = usuarioRepository.findById(17).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Asignar el usuario a la publicación
        publicacion.setUsuarioIdusuario(usuario);

        // Asignar la fecha de creación automáticamente
        publicacion.setFechaCreacion(LocalDate.now());

        // Guardar la publicación en la base de datos
        publicacionRepository.save(publicacion);

        // Redirigir a la lista de publicaciones después de crear la nueva publicación
        return "redirect:/usuario/comunidad";
    }

    @GetMapping("/nuevaReseña")
    public String nuevaResenha(){
        return "Usuario/nuevaReseña-usuario";
    }

    @GetMapping("/categoriaMujer")
    public String showMujerCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductRopaMujer());
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(1));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(1));
        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaHombre")
    public String showHombreCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductRopaHombre());
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(2));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(2));
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaTecnologia")
    public String showTecnologiaCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductElectronico());
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));
        return "Usuario/CategoriaTecnologia-usuario";
    }
    @GetMapping("/categoriaMuebles")
    public String showMuebleCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductMuebles());
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(4));
        return "Usuario/CategoriaMuebles-usuario";
    }
    @GetMapping("/categoriaMueblesFilter")
    public String showMuebleCategoriaFilter(Model model,
                                            @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                            @RequestParam(value = "material", required = false) List<String> material,
                                            @RequestParam(value = "precioMin", required = false) Double precioMin,
                                            @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductMueblesFilter(categoria, material, precioMin, precioMax));
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(4));

        return "Usuario/CategoriaMuebles-usuario";
    }

    @GetMapping("/categoriaHombreFilter")
    public String showHombreCategoriaFilter(Model model,
                                            @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                            @RequestParam(value = "material", required = false) List<String> material,
                                            @RequestParam(value = "precioMin", required = false) Double precioMin,
                                            @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductHombresFilter(categoria, material, precioMin, precioMax));
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(2));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(2));

        return "Usuario/CategoriaHombre-usuario";
    }

    @GetMapping("/categoriaMujerFilter")
    public String showMujerCategoriaFilter(Model model,
                                           @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                           @RequestParam(value = "material", required = false) List<String> material,
                                           @RequestParam(value = "precioMin", required = false) Double precioMin,
                                           @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductMujerFilter(categoria, material, precioMin, precioMax));
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(1));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(1));

        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaTecnologiaFilter")
    public String showTecnologiaCategoriaFilter(Model model,
                                                @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                                @RequestParam(value = "material", required = false) List<String> material,
                                                @RequestParam(value = "precioMin", required = false) Double precioMin,
                                                @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductMujerFilter(categoria, material, precioMin, precioMax));
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));

        return "Usuario/CategoriaTecnologia-usuario";
    }



}
