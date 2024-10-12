package com.app.tradogt.controller;


import com.app.tradogt.dto.OrdenCompraUserDto;
import com.app.tradogt.entity.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.query.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.tradogt.repository.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/usuario")
public class UsuarioFinalController {
    public static class RandomCodeGenerator {

        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final SecureRandom RANDOM = new SecureRandom();

        public static String generateRandomCode(int length) {
            StringBuilder code = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int index = RANDOM.nextInt(CHARACTERS.length());
                code.append(CHARACTERS.charAt(index));
            }
            return code.toString();
        }
    }

    final ProductosRepository productosRepository;
    final OrdenRepository ordenRepository;
    final UsuarioRepository usuarioRepository;
    //final ProductoEnZonaEnOrdenRepository productoEnZonaEnOrdenRepository;
    final ProductoEnZonaRepository productoEnZonaRepository;
    final PublicacionRepository publicacionRepository;
    final ComentarioRepository comentarioRepository;
    final SubCategoriaRepository subCategoriaRepository;
    final CarritoRepository carritoRepository;
    final PagoRepository pagoRepository;
    final ResenaRepository resenaRepository;
    final ProductoEnCarritoRepository productoEnCarritoRepository;
    final AutenticacionRepository autenticacionRepository;
    final EstadoOrdenRepository estadoOrdenRepository;

    public UsuarioFinalController(ProductosRepository productosRepository, OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, ProductoEnZonaRepository productoEnZonaRepository, PublicacionRepository publicacionRepository, ComentarioRepository comentarioRepository, SubCategoriaRepository subCategoriaRepository, CarritoRepository carritoRepository, PagoRepository pagoRepository, ResenaRepository resenaRepository, ProductoEnCarritoRepository productoEnCarritoRepository, AutenticacionRepository autenticacionRepository, EstadoOrdenRepository estadoOrdenRepository) {
        this.productosRepository = productosRepository;
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
        //this.productoEnZonaEnOrdenRepository = productoEnZonaEnOrdenRepository;
        this.productoEnZonaRepository = productoEnZonaRepository;
        this.publicacionRepository = publicacionRepository;
        this.comentarioRepository = comentarioRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.carritoRepository = carritoRepository;
        this.pagoRepository = pagoRepository;
        this.resenaRepository = resenaRepository;
        this.productoEnCarritoRepository = productoEnCarritoRepository;
        this.autenticacionRepository = autenticacionRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
    }

    //Metodo auxiliar para obtener el id del usuario
    private int getAuthenticatedUserId() {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;

        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername(); // Obtener el correo del usuario autenticado
        } else {
            correoUsuario = principal.toString();
        }

        // Buscar el usuario (agente) en la base de datos usando el correo
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);  // Método para buscar usuario por correo
        return usuario.getId(); // Retornar el ID del agente autenticado
    }

    @GetMapping("/inicio")
    public String inicio() {

        //Listar los pedidos recientes

        return "Usuario/inicio-usuario";
    }

    @GetMapping("/misPedidos")
    public String misPedidos(Model model) {
        int idUsuario = getAuthenticatedUserId();
        // Usuario user = usuarioRepository.findById(idUsuario).get();
        List<Object[]> resultados = ordenRepository.findOrdersByUsuarioIdusuario(idUsuario);
        List<OrdenCompraUserDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraUserDto(
                        (Integer) result[0],  // idOrden
                        (String) result[1],  // codigoOrden
                        (BigDecimal) result[2],  // costoTotal
                        ((java.sql.Date) result[3]).toLocalDate(),  // fechaCreacion
                        (String) result[4],  // estadoOrden
                        result[5] != null ? (String) result[5] : "No asignado",  // agente (manejo de null)
                        result[6] != null ? (int) ((Byte) result[6]).byteValue() : 0)// valoracion
                )
                .collect(Collectors.toList());
        // Añadir la lista de órdenes al modelo para ser usada en la vista
        model.addAttribute("listaOrdenes", listaOrdenes);
        // model.addAttribute("listaPedidos", resultados);
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
            Optional<Orden> miOrden = ordenRepository.findByCodigo(codigo);
            //Obtener el id del usuario
            // int idUsuario = getAuthenticatedUserId();
           // Usuario usuario = usuarioRepository.findById(idUsuario).get();
            //List<Carrito> miCarrito = carritoRepository.findByUsuarioIdusuario(usuario);

            if(miOrden.isPresent()) {
                String correoUsuario=  miOrden.get().getCarritoIdcarrito().getUsuarioIdusuario().getCorreo();
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
    public String verPerfil(Model model){

        Integer userId = getAuthenticatedUserId();

        // Buscar el usuario en la base de datos
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // Añadir el usuario al modelo para que esté disponible en la vista
            model.addAttribute("usuario", usuario);
        } else {
            // Manejar el caso donde el usuario no existe
            return "error/usuario_no_encontrado"; // O una página de error personalizada
        }


        return "Usuario/profile_user";
    }

    @GetMapping("/editProfile")
    public String editProfile(Model model) {

        //Obtengo el id del usuario
        Integer userId = getAuthenticatedUserId();
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            model.addAttribute("usuario", usuario);

        }

        return "Usuario/profile_user_edit";
    }

    @PostMapping("/subirFoto")
    private String subirFoto(@RequestParam("foto") MultipartFile file, Model model) throws IOException {

        int user = getAuthenticatedUserId();

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(user);
        if (usuarioOptional.isPresent()) {
            // Ruta donde se guardarán las imágenes (ajusta según tu estructura)
            String uploadDir = "src/main/resources/static/images/users/";

            // Guardar el archivo en la ruta definida
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            Files.write(path, bytes);

            Usuario usuario = usuarioOptional.get();
            usuario.setFoto(file.getOriginalFilename());
            usuarioRepository.save(usuario);
        }

        return "redirect:/usuario/perfil";
    }

    @PostMapping("/saveProfile")
    private String saveProfile(Model model,
                               @RequestParam(value = "telefono", required = false) String telefono,
                               @RequestParam(value = "direccion", required = false) String direccion) {

        int user = getAuthenticatedUserId();
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(user);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // Verifica si el campo teléfono no es nulo ni está vacío
            if (telefono != null && !telefono.isEmpty()) {
                usuario.setTelefono(telefono);
            }
            // Verifica si el campo dirección no es nulo ni está vacío
            if (direccion != null && !direccion.isEmpty()) {
                usuario.setDireccion(direccion);
            }
            // Guarda solo si hay cambios
            usuarioRepository.save(usuario);
        }
        return "redirect:/usuario/perfil";
    }

    @GetMapping("/editarOrdenes")
    public String formularioPedido() {
        return "Usuario/formOrdenes";
    }

    @GetMapping("/tracking")
    public String tracking(@RequestParam("id") int id, Model model) {

        //Lista de todos los productos de mi orden
        Optional<Orden> orden = ordenRepository.findById(id);
        if (orden.isPresent()) {
            Orden ord = orden.get();
            model.addAttribute("orden", ord);
            Carrito carrito = orden.get().getCarritoIdcarrito();

            //Obtener el id de carrito
            int items = ord.getCarritoIdcarrito().getId();
            //Listar mis productos
            List<ProductoEnCarrito> mispedidos = productoEnCarritoRepository.findBycarritoIdcarrito(carrito);
            model.addAttribute("listaPedidos", mispedidos);

            //usuario
            int usuarioId = getAuthenticatedUserId();
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            model.addAttribute("usuario", usuario.get());


            //Obtener el costo total
            BigDecimal monto = ord.getPagoIdpago().getMonto();
            model.addAttribute("monto", monto);

        }else {
            System.out.println("Orden no encontrada"); }

        return "Usuario/trackingOrd";
    }

    @GetMapping("/editOrden")
    public String editOrden(@RequestParam("id") int id, Model model) {

        //Lista de todos los productos de mi orden
        Optional<Orden> orden = ordenRepository.findById(id);
        if (orden.isPresent()) {
            Orden ord = orden.get();
            model.addAttribute("orden", ord);
            Carrito carrito = orden.get().getCarritoIdcarrito();

            //Obtener el id de carrito
            int items = ord.getCarritoIdcarrito().getId();
            //Listar mis productos
            List<ProductoEnCarrito> mispedidos = productoEnCarritoRepository.findBycarritoIdcarrito(carrito);
            model.addAttribute("listaPedidos", mispedidos);

            //usuario
            int usuarioId = getAuthenticatedUserId();
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            model.addAttribute("usuario", usuario.get());


            //Obtener el costo total
            BigDecimal monto = ord.getPagoIdpago().getMonto();
            model.addAttribute("monto", monto);

        }else {
            System.out.println("Orden no encontrada"); }

        return "Usuario/trackingOrdEdit";
    }

    @PostMapping("/updateDireccion")
    public String updateDireccion(@RequestParam("direccion") String direccion, Model model) {
        // Aquí debes obtener el usuario actual, por ejemplo, desde la sesión o un parámetro

        int id = getAuthenticatedUserId();

        Optional<Usuario> myuser = usuarioRepository.findById(id);
        myuser.get().setDireccion(direccion);
        usuarioRepository.save(myuser.get());

        return "Usuario/listaOrdenes";
    }


    @GetMapping("/productoDetalles/{id}")
    public String showProductoDetalles(@PathVariable int id, Model model) {
        // Buscar el producto por id
        Optional<Producto> productoOpt = productosRepository.findById(id);

        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            // Si el producto existe, agregarlo al modelo
            model.addAttribute("product",producto);
            model.addAttribute("currentId",id);

            switch(producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getId()) {
                case 1:
                    model.addAttribute("productList", productosRepository.findProductRopaMujer());
                    break;
                case 2:
                    model.addAttribute("productList", productosRepository.findProductRopaHombre());
                    break;
                case 3:
                    model.addAttribute("productList", productosRepository.findProductElectronico());
                    break;
                case 4:
                    model.addAttribute("productList", productosRepository.findProductMuebles());
                    break;
                default:
                    model.addAttribute("productList", productosRepository.findAll());
                    break;
            }


            return "Usuario/producto-detalles";

            // Devuelve la vista con el producto
        } else {
            // Si no se encuentra el producto, redirige o muestra una página de error
            return "redirect:/usuario/inicio"; // Podrías crear una página de error personalizada
        }
    }

    @GetMapping("/carrito")
    public String showCarrito( Model model) {

        //Usuario
        int user = getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(user).get();

        Carrito miCarrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(usuario, (byte) 0);
        if (miCarrito != null) {
            List<ProductoEnCarrito> misProductos = productoEnCarritoRepository.findBycarritoIdcarrito( miCarrito);
            if(misProductos.isEmpty()) {
                model.addAttribute("mensaje", "El carrito se encuentra vacío");
            }else {
                model.addAttribute("carrito", misProductos);

                ProductoEnCarrito item = misProductos.get(0);
                BigDecimal costoEnvio = item.getProductoEnZona().getCostoEnvio();
                model.addAttribute("costoEnvio", costoEnvio);
            }

        }else {
            model.addAttribute("mensaje", "El carrito se encuentra vacío");
            return "Usuario/carrito-usuario";
        }

        return "Usuario/carrito-usuario";
    }
   /* @PostMapping("/actualizarCantidad")
    public String actualizarCantidad (
            @RequestParam("costoTotal") BigDecimal costoTotal,
            @RequestParam("costoEnvio") BigDecimal costoEnvio,
            @RequestParam("total") BigDecimal total,
            @RequestParam("cantidad") int cantidad,
            Model model) {

        int id = getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(id).get();
        Carrito micarrito = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario,(byte) 0);

        micarrito.setCostoTotal(total);
        carritoRepository.save(micarrito);
        List<ProductoEnCarrito> misproductos = productoEnCarritoRepository.findBycarritoIdcarrito(micarrito);
        misproductos.get(0).setCantidad(cantidad);
        productoEnCarritoRepository.save(misproductos.get(0));

        //Obtener la zona del usuario
        Zona zone = usuario.getZonaIdzona();
        //obtener el id del producto
        int idproduct = micarrito.getId();

        //listar los productos en zona
        Optional<ProductoEnZona> almacen = productoEnZonaRepository.findByProductoIdproductoAndZonaIdzona(, zone);
        int totalProducto = almacen.get().getCantidad();
        int newTotal = totalProducto - cantidad;
        if (newTotal >= 25) {
            almacen.get().setEstadoRepo((byte) 0);
            almacen.get().setCantidad(newTotal);

        }else {
            almacen.get().setEstadoRepo((byte) 1);
            almacen.get().setCantidad(newTotal);
        }
        productoEnZonaRepository.save(almacen.get());

        return "redirect:/usuario/checkout-info";
    }*/




    /*
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
        if (!productosEnCarrito.isEmpty()) {

            //carritoRepository.deleteAll(productosEnCarrito);
            Carrito item = productosEnCarrito.get(0);
            item.setIsDelete(true);
            carritoRepository.save(item);
        }

        // Añade un mensaje de éxito
        redirectAttributes.addFlashAttribute("message", "Producto eliminado exitosamente");

        return "redirect:/usuario/carrito";
    }
*/


    @GetMapping("/ordenCompra")
    public String showordenCompra() {
        return "Usuario/orden-compra-usuario";
    }

    @GetMapping("/reseñas")
    public String showResenhas(Model model) {
        List<Resena> resenas = resenaRepository.findAll();
        model.addAttribute("resenas", resenas);

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

    @GetMapping("/verReseña/{id}")
    public String showDetalleResena(@PathVariable("id") Integer id, Model model) {
        // Buscar la reseña por id
        Resena resena = resenaRepository.findById(id).orElse(null);

        // Si la reseña no se encuentra, puedes redirigir a una página de error o similar
        if (resena == null) {
            return "redirect:/error";  // Página de error personalizada
        }

        // Pasar la reseña al modelo para que esté disponible en la vista
        model.addAttribute("resena", resena);

        // Nombre de la vista Thymeleaf
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
    public String nuevaResenha(Model model){
        return "Usuario/nuevaReseña-usuario";
    }

    @GetMapping("/categoriaMujer")
    public String showMujerCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductRopaMujer());
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));

        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(1));
        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaHombre")
    public String showHombreCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductRopaHombre());
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(2));
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaTecnologia")
    public String showTecnologiaCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductElectronico());
        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));
        return "Usuario/CategoriaTecnologia-usuario";
    }
    @GetMapping("/categoriaMuebles")
    public String showMuebleCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductMuebles());
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(4));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(4));
        return "Usuario/CategoriaMuebles-usuario";
    }
    @GetMapping("/categoriaMueblesFilter")
    public String showMuebleCategoriaFilter(Model model,
                                            @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                            @RequestParam(value = "material", required = false) List<String> material,
                                            @RequestParam(value = "marca", required = false) List<String> marca,
                                            @RequestParam(value = "precioMin", required = false) Double precioMin,
                                            @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductMueblesFilter(categoria, material, marca, precioMin, precioMax));
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(4));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(4));
        return "Usuario/CategoriaMuebles-usuario";
    }

    @GetMapping("/categoriaHombreFilter")
    public String showHombreCategoriaFilter(Model model,
                                            @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                            @RequestParam(value = "material", required = false) List<String> material,
                                            @RequestParam(value = "color", required = false) List<String> color,
                                            @RequestParam(value = "marca", required = false) List<String> marca,
                                            @RequestParam(value = "precioMin", required = false) Double precioMin,
                                            @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductHombresFilter(categoria, material,marca,color, precioMin, precioMax));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));
        return "Usuario/CategoriaHombre-usuario";
    }



    @GetMapping("/categoriaMujerFilter")
    public String showMujerCategoriaFilter(Model model,
                                           @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                           @RequestParam(value = "talla", required = false) List<String> talla,
                                           @RequestParam(value = "color", required = false) List<String> color,
                                           @RequestParam(value = "marca", required = false) List<String> marca,
                                           @RequestParam(value = "precioMin", required = false) Double precioMin,
                                           @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductMujerFilter(categoria, talla,marca, color,precioMin, precioMax));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(1));

        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaTecnologiaFilter")
    public String showTecnologiaCategoriaFilter(Model model,
                                                @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                                @RequestParam(value = "almacenamiento", required = false) List<String> almacenamiento,
                                                @RequestParam(value = "ram", required = false) List<String> ram,
                                                @RequestParam(value = "marca", required = false) List<String> marca,
                                                @RequestParam(value = "precioMin", required = false) Double precioMin,
                                                @RequestParam(value = "precioMax", required = false) Double precioMax) {
        model.addAttribute("productList", productosRepository.findProductElectroFilter(categoria, almacenamiento,ram,marca,        precioMin, precioMax));
        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        return "Usuario/CategoriaTecnologia-usuario";
    }

    @GetMapping("/categoriaHombreSearch")
    public String showHombreCategoriaBuscar(Model model,


                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(query,2));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaMujerSearch")
    public String showMujerCategoriaBuscar(Model model,
                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(query,1));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));
        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaTecnologiaSearch")
    public String showTecnologiaCategoriaBuscar(Model model,


                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(query,3));
        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaMuebleSearch")
    public String showMuebleCategoriaBuscar(Model model,


                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(query,4));
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(4));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(4));
        return "Usuario/CategoriaMuebles-usuario";
    }
    @GetMapping("/checkout-info")
    public String showcheckout(Model model) {

        int user = getAuthenticatedUserId();
        Usuario myuser = usuarioRepository.findByIdUsuario(user);

        model.addAttribute("usuario", myuser);

        //Ingresar los datos de nuestro carrito actual
        Carrito miCarrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(myuser, (byte) 0);
        BigDecimal total = miCarrito.getCostoTotal();
        model.addAttribute("total", total); //Se obtiene el costo total

        if (miCarrito != null) {
            List<ProductoEnCarrito> misProductos = productoEnCarritoRepository.findBycarritoIdcarrito( miCarrito);
            if(misProductos.isEmpty()) {
                model.addAttribute("mensaje", "El carrito se encuentra vacío");
            }else {
                model.addAttribute("carrito", misProductos);

                ProductoEnCarrito item = misProductos.get(0);
                BigDecimal costoEnvio = item.getProductoEnZona().getCostoEnvio();
                model.addAttribute("costoEnvio", costoEnvio);
            }

        }else {
            model.addAttribute("mensaje", "El carrito se encuentra vacío");
            return "Usuario/carrito-usuario";
        }
        return "Usuario/billing-info-usuario";
    }

    //Guardar los datos de pago
    @PostMapping("/savePayment")
    private String showSavePayment(@RequestParam("nombre") String nombre,
                                   @RequestParam("apellido") String apellido,
                                   @RequestParam("correo") String correo,
                                   @RequestParam("telefono") String telefono,
                                   @RequestParam("dni") String dni,
                                   @RequestParam("distrito") String distrito,
                                   @RequestParam("direccion") String direccion,
                                   @RequestParam("metodo") String metodo,
                                   @RequestParam("nombreTarjeta") String nombreTarjeta,
                                   @RequestParam("numeroTarjeta") String numeroTarjeta,
                                   @RequestParam("fechaTarjeta") String fechaTarjeta,
                                   @RequestParam("codigoCVV") String codigoCVV,
                                   @RequestParam("monto") BigDecimal monto,
                                   Model model,  RedirectAttributes attr){

        System.out.println("-------------------");
        System.out.println("Nombre" + nombre);

        Autenticacion facturacion = new Autenticacion();
        facturacion.setNombre(nombre);
        facturacion.setApellido(apellido);
        facturacion.setCorreo(correo);
        facturacion.setTelefono(telefono);
        facturacion.setDni(dni);
        facturacion.setDistrito(distrito);
        facturacion.setDireccion(direccion);

        autenticacionRepository.save(facturacion);
        int user = getAuthenticatedUserId();
        Usuario myuser = usuarioRepository.findByIdUsuario(user);

        // Crear una instancia de Pago y asignar los valores del formulario
        Pago pago = new Pago();
        pago.setMetodo(metodo);
        pago.setNombreTarjeta(nombreTarjeta);
        pago.setNumeroTarjeta(numeroTarjeta);
        pago.setFechaTarjeta(fechaTarjeta);
        pago.setCodigoCVV(codigoCVV);
        pago.setMonto(monto);
        pago.setFechaTarjeta(fechaTarjeta);
        pago.setFecha(LocalDate.now());
        pago.setAutenticacionIdautenticacion(facturacion);
        System.out.println("Pago antes de guardar: " + pago.toString());
        List<Pago> listaPago = pagoRepository.findAll();
        pago.setId(listaPago.size() +1);
        pagoRepository.save(pago);
        //Se genera una nueva orden
        Orden orden = new Orden();
        List<EstadoOrden> listaEstadoOrden = estadoOrdenRepository.findAll();
        EstadoOrden primerEstadoOrden = listaEstadoOrden.get(0);

        //Listar agentes de compra
        List<Usuario> listaAgente = usuarioRepository.findAllByRolIdrolIdAndIsActivated(3, (byte) 1);
        //Cambio al id de orden del carrito a null -> k
        Carrito carrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(myuser, (byte) 0 );

        Usuario agente = null;
        if (!listaAgente.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(listaAgente.size());
            orden.setEstadoordenIdestadoorden(primerEstadoOrden);
            orden.setFechaCreacion(LocalDate.now());
            orden.setFechaArribo(LocalDate.now().plusWeeks(2));
            orden.setIsDeleted((byte) 0);
            orden.setCodigo(RandomCodeGenerator.generateRandomCode(5));
            orden.setCostoTotal(monto);
            orden.setPagoIdpago(pago);
            orden.setUsuarioIdusuario(myuser);
            orden.setCarritoIdcarrito(carrito);

        }
        // Guardar la orden
        ordenRepository.save(orden);
        Integer idOrden = orden.getId();
        attr.addFlashAttribute("exito", "Se ha generado correctamente la orden de compra!");
        model.addAttribute("orden", orden);




        if (carrito != null) {
            carrito.setIsDelete((byte) 1);
            carritoRepository.save(carrito);
        }
        System.out.println("ID de la orden generada: " + idOrden);
        return "redirect:/usuario/misPedidos";
    }

}
