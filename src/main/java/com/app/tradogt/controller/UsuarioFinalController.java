package com.app.tradogt.controller;


import com.app.tradogt.dto.OrdenCompraUserDto;
import com.app.tradogt.dto.PasswordChangeDto;
import com.app.tradogt.entity.*;
import com.app.tradogt.services.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.app.tradogt.repository.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/usuario")
public class UsuarioFinalController {

    private Optional<Usuario> authenticatedUser;
    private int zonaId;
   // @Autowired
    //private MinValidatorForCharSequence minValidatorForCharSequence;

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

    @Autowired
    private StorageService storageService;

    @Autowired
    private PasswordEncoder passwordEncoder;


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

    @ModelAttribute
    public void setAuthenticatedUser() {
        int userId = getAuthenticatedUserId(); // Obtén el ID del usuario autenticado
        Optional<Usuario> authenticatedUser = usuarioRepository.findById(userId);

        // Verifica si el usuario está presente y asigna el zonaId
        authenticatedUser.ifPresent(user -> {
            this.authenticatedUser = authenticatedUser; // Asigna el usuario autenticado
            this.zonaId = user.getDistritoIddistrito().getZonaIdzona().getId(); // Asigna al campo de clase
        });
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
    public String editProfile(Model model, RedirectAttributes attr) {

        Usuario usuario = usuarioRepository.findById(getAuthenticatedUserId()).get();
        model.addAttribute("usuario", usuario);
        return "Usuario/profile_user_edit";

    }


    @PostMapping("/subirFoto")
    public String viewSubirFoto(@RequestParam("foto") MultipartFile file, Model model) throws IOException {
        System.out.println("Entré al controler");

        // Ruta dinámica donde se guardarán las imágenes (fuera de static)
        String uploadDir = "uploads/fotosUsuarios/";

        // Guardar el archivo en la ruta definida
        byte[] bytes = file.getBytes();
        Path path = Paths.get(uploadDir + file.getOriginalFilename());
        Files.write(path, bytes);
        System.out.println("Guardé la foto en: " + path);

        // Obtener el usuario autenticado desde el modelo
        Usuario usuario = (Usuario) model.getAttribute("usuarioAutenticado");
        assert usuario != null;

        // Actualizar el nombre de la foto en la base de datos
        usuario.setFoto(file.getOriginalFilename());
        System.out.println("Seteé la foto como: " + file.getOriginalFilename());
        usuarioRepository.save(usuario);
        System.out.println("Guardé el usuario");

        // Redirigir al perfil
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
    public String tracking(@RequestParam("id")String code, Model model) {

        //Lista de todos los productos de mi orden
        Optional<Orden> orden = ordenRepository.findByCodigo(code);
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

            //Obtener el costo de envio
            BigDecimal costoEnvio = mispedidos.get(0).getProductoEnZona().getCostoEnvio();
            model.addAttribute("costoEnvio", costoEnvio);

        }else {
            System.out.println("Orden no encontrada"); }

        return "Usuario/trackingOrd";
    }

    @GetMapping("/editOrden")
    public String editOrden(@RequestParam("id") String code, Model model) {

        //Lista de todos los productos de mi orden
        Optional<Orden> orden = ordenRepository.findByCodigo(code);
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

            //Obtener el costo de envio
            BigDecimal costoEnvio = mispedidos.get(0).getProductoEnZona().getCostoEnvio();
            model.addAttribute("costoEnvio", costoEnvio);

        }else {
            System.out.println("Orden no encontrada"); }

        return "Usuario/trackingOrdEdit";
    }

    //Asignación de un Agente
    @PostMapping("/asignarAgente")
    public String asignarAgente(@RequestParam("codigoOrden") String code,
                                @RequestParam("id") int id,  RedirectAttributes attr) {
        Optional<Orden> orden = ordenRepository.findByCodigo(code);
        //Listar agentes de compra
        List<Usuario> listaAgente = usuarioRepository.findAllByRolIdrolIdAndIsActivated(3, (byte) 1);
        Optional<EstadoOrden> newEstado = estadoOrdenRepository.findById(2);
        Usuario agente = null;
        if (orden.isPresent()) {
            Orden ord = orden.get();
            Random random = new Random();
            int index = random.nextInt(listaAgente.size());
            agente = listaAgente.get(index);
            ord.setAgentcompraIdusuario(agente);
            //Cuando se le asigna un agente --> el estado cambia a En validación
            ord.setEstadoordenIdestadoorden(newEstado.get());
            //Se guarda la fecha del cambio de estado
            ord.setFechaValidacion(LocalDate.now());
            System.out.println("fecha de validacion: " + ord.getFechaValidacion());
            ordenRepository.save(ord);
            attr.addFlashAttribute("msjAgente", "Se le ha asignado el agente " + agente.getNombre() + " " + agente.getApellido() + " en la orden " + code);
            return "redirect:/usuario/misPedidos";
        }
        return "redirect:/usuario/misPedidos";
    }

    //Actualizar la dirreción de entrega
    @PostMapping("/updateDelivery")
    public String updateDireccion(@RequestParam("idOrden") int idOrden,
                                  @RequestParam("idUsuario") int idUsuario,
                                  @RequestParam("direccion") String direccionEntrega,
                                  RedirectAttributes redirectAttributes){

        //Buscamos la orden por id
        Optional<Orden> orden = ordenRepository.findById(idOrden);
        String code = orden.get().getCodigo();

        System.out.println("-----------");
        System.out.println("codigo: " + code);


        if(orden.isPresent()) {
            Orden ord = orden.get();
            ord.setLugarEntrega(direccionEntrega);
            ordenRepository.save(ord);
            // Agregar mensaje de éxito al flash attributes
            redirectAttributes.addFlashAttribute("saveEdit", "La dirección de entrega ha sido modificada con éxito.");
        }else {
            // Si no se encuentra el usuario
            redirectAttributes.addFlashAttribute("saveEditError", "La orden no fue encontrada. Inténtelo de nuevo más tarde");
        }

        return "redirect:/usuario/misPedidos";
    }


  /*  @PostMapping("/saveProfile") //perfil
    public String updateDireccionP(@Valid @ModelAttribute("usuario") Usuario usuario,
                                  BindingResult result) {
       if(result.hasErrors()){
           //Si hay errores de validación, redirige a la vista del formulario
           return "Usuario/profile_user_edit";
       }

       Usuario myuser = usuarioRepository.findByIdUsuario(usuario.getId());

        System.out.println("---------------");
        System.out.println("nombre: " + myuser.getNombre());
        System.out.println("apellido: " + myuser.getApellido());

       usuarioRepository.save(usuario);

        return "redirect: /usuario/inicio";
    }*/

    @GetMapping("/productoDetalles")
    public String showProductoDetalles(@RequestParam("id") int id, Model model) {
        // Buscar el producto por id
        int id2 = getAuthenticatedUserId();
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id2);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Optional<ProductoEnZona> productoEnZona = productoEnZonaRepository.findByIdAndZona(id, usuario.getDistritoIddistrito().getZonaIdzona().getId());

            if (productoEnZona.isPresent()) {
                model.addAttribute("productoDetalles", productoEnZona.get());
            } else {
                return "redirect:/usuario/inicio";
            }
        } else {
            // Manejar el caso en que no se encuentra el usuario
            return "redirect:/usuario/inicio";
        }
        Optional<Producto> productoOpt = productosRepository.findById(id);



        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            // Si el producto existe, agregarlo al modelo
            model.addAttribute("product",producto);
            model.addAttribute("rating", resenaRepository.findRating(id));
            model.addAttribute("conteoRating", resenaRepository.countResena(id));
            model.addAttribute("currentId",id);

            switch(producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getId()) {
                case 1:
                    model.addAttribute("productList", productosRepository.findProductRopaMujer(zonaId));
                    break;
                case 2:
                    model.addAttribute("productList", productosRepository.findProductRopaHombre(zonaId));
                    break;
                case 3:
                    model.addAttribute("productList", productosRepository.findProductElectronico(zonaId));
                    break;
                case 4:
                    model.addAttribute("productList", productosRepository.findProductMuebles(zonaId));
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

    //Seleción de producto al carrito
   @PostMapping("/selecionarProducto")
    public String selecionarProducto(@RequestParam("productoId") int productoId,
                                     @RequestParam("cantidad") String cantidadOculta , RedirectAttributes attr, Model model) {

        //Busco un carrito creado a mi id
        int idUser= getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(idUser).get();
       System.out.println("usuario: " + usuario.getNombre());
        int zonaid = usuario.getDistritoIddistrito().getZonaIdzona().getId();
       System.out.println("zonaid: " + zonaid);
       int cantidadP = Integer.parseInt(cantidadOculta);

       //Nombre del producto
       Producto producto = productosRepository.findById(productoId).get();

        Carrito hayCarrito = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario, (byte) 0);
       System.out.println("hay carrito?: " + hayCarrito);
       //Añadimos el producto al carrito nuevo
       ProductoEnCarrito misProductoEnCarrito = new ProductoEnCarrito();
       //Producto en Zona (TIENDA)
       Optional<ProductoEnZona> productoEnZona = productoEnZonaRepository.findByIdAndZona(productoId, usuario.getDistritoIddistrito().getZonaIdzona().getId());

       int stock = productoEnZona.get().getCantidad();

       ProductoEnCarritoId id_ProductoEnCarrito = new ProductoEnCarritoId();
       id_ProductoEnCarrito.setProductoenzonaProductoIdproducto(productoId);
       id_ProductoEnCarrito.setProductoenzonaZonaIdzona(zonaid);

       if(stock>=cantidadP) {
           if(hayCarrito != null) {
               //Hay carrito --> Añadimos el producto
               Carrito miCarritoActual = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario, (byte) 0);

               //Añado un nuevo producto
               id_ProductoEnCarrito.setCarritoIdcarrito(miCarritoActual.getId());
               //mensaje
               attr.addFlashAttribute("mensajeProductNuevo", "Se ha añadido un nuevo producto: " + producto.getNombre());
               misProductoEnCarrito.setCantidad(cantidadP);
               misProductoEnCarrito.setCarritoIdcarrito(miCarritoActual);
               misProductoEnCarrito.setId(id_ProductoEnCarrito);
               misProductoEnCarrito.setProductoEnZona(productoEnZona.get());
               productoEnCarritoRepository.save(misProductoEnCarrito);

           }else{
               //No hay carrito --> Creamos uno nuevo
               Carrito miCarritoNuevo = new Carrito();
               miCarritoNuevo.setUsuarioIdusuario(usuario);
               miCarritoNuevo.setIsDelete((byte) 0);
               carritoRepository.save(miCarritoNuevo);
               id_ProductoEnCarrito.setCarritoIdcarrito(miCarritoNuevo.getId());
               //mensaje
               attr.addFlashAttribute("mensajeProductNuevo", "Se ha añadido un nuevo producto: " + producto.getCodigo() +' ' + producto.getNombre());

               misProductoEnCarrito.setCantidad(cantidadP);
               misProductoEnCarrito.setCarritoIdcarrito(miCarritoNuevo);
               misProductoEnCarrito.setId(id_ProductoEnCarrito);
               misProductoEnCarrito.setProductoEnZona(productoEnZona.get());
               productoEnCarritoRepository.save(misProductoEnCarrito);
           }
       }else {
           //mensaje de no hay stock suficiente
           attr.addFlashAttribute("msgError", "No hay stock suficiente para este producto");
       }
       // Redirige al detalle del producto
       return "redirect:/usuario/productoDetalles?id=" + productoId;
    }

    @GetMapping("/carrito")
    public String showCarrito( Model model) {

        //Usuario
        int user = getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(user).get();
        model.addAttribute("usuario", usuario);

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
   @PostMapping("/actualizarCantidad") //Aquí se debe guardar una lista o array de las cantidades de cada producto.
    public String actualizarCantidad (
            @RequestParam("total") BigDecimal total,
            @RequestParam("cantidad") List<Integer> cantidad) {
        System.out.println(cantidad);

        int id = getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(id).get();
        Carrito micarrito = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario,(byte) 0);

        micarrito.setCostoTotal(total);
        carritoRepository.save(micarrito);
        List<ProductoEnCarrito> misproductos = productoEnCarritoRepository.findBycarritoIdcarrito(micarrito);
           for (int i = 0; i < misproductos.size(); i++) {
               // Actualiza la cantidad del producto
               misproductos.get(i).setCantidad(cantidad.get(i));
               // Guarda el producto actualizado en el repositorio
               productoEnCarritoRepository.save(misproductos.get(i));
           }

        //Obtener la zona del usuario
        Zona zone = usuario.getZonaIdzona();
        //obtener el id del producto
        int idproduct = micarrito.getId();

        //CORRREGIRRRRRRRRRRRRR AQUIIIIIIIIII


        return "redirect:/usuario/checkout-info";
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

        //Busco mi carrito
        Carrito miCarrito = carritoRepository.findByUsuarioIdusuarioAndIsDelete(myUser.get(), (byte) 0);

        //Busco el id del producto en ProductoEnZona
        Optional<ProductoEnZona> tienda = productoEnZonaRepository.findByIdAndZona(productoId, myUser.get().getDistritoIddistrito().getZonaIdzona().getId());
        System.out.println("----------------------------");
        System.out.println("producto en zona id: " + tienda.get().getId());

        ProductoEnCarritoId item = new ProductoEnCarritoId();
        item.setCarritoIdcarrito(miCarrito.getId());
        item.setProductoenzonaZonaIdzona(myUser.get().getDistritoIddistrito().getZonaIdzona().getId());
        item.setProductoenzonaProductoIdproducto(productoId);
        System.out.println("------------");
        System.out.println("ID Carrito: " + item.getCarritoIdcarrito());
        System.out.println("ID Zona: " + item.getProductoenzonaZonaIdzona());
        System.out.println("ID Producto: " + item.getProductoenzonaProductoIdproducto());
        //Obtenego el producto
        Optional<ProductoEnCarrito> itemDelete = productoEnCarritoRepository.findById(item);
        productoEnCarritoRepository.delete(itemDelete.get());
        System.out.println("Se ha borrado el producto :D");
        // Añade un mensaje de éxito
        redirectAttributes.addFlashAttribute("message", "El producto " + myProduct.get().getCodigo()+" ha sido eliminado de forma exitosa.");
        return "redirect:/usuario/carrito";
    }

    @GetMapping("/reseñas")
    public String showResenhas(Model model) {
        List<Resena> resenas = resenaRepository
                .findByUsuarioIdusuarioIsAcceptedAndUsuarioIdusuarioIsPostulatedAndUsuarioIdusuarioIsActivated(1, 0, 1);

        model.addAttribute("resenas", resenas);

        // Si no hay reseñas, puedes mostrar un mensaje
        if (resenas.isEmpty()) {
            model.addAttribute("noResultsMessage", "Reseña no encontrada");
        }
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
        int id = getAuthenticatedUserId();


        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        // Crear un nuevo comentario
        Comentario comentario = new Comentario();
        comentario.setCuerpo(cuerpo);
        comentario.setFechaCreacion(LocalDate.now());  // Fecha actual
        comentario.setPublicacionIdpublicacion(publicacion);

        Usuario usuario = usuarioRepository.findById(id)
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
    public String registroPostulacion(Model model) {
        int id = getAuthenticatedUserId();



        // Obtener el usuario logueado de la base de datos
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        // Añadir los datos del usuario al modelo para mostrarlos en la vista
        model.addAttribute("usuario", usuario);

        return "Usuario/registroSolicitud";
    }

    @PostMapping("/registro")
    public String procesarPostulacion(
            @RequestParam("razonSocial") String razonSocial,
            @RequestParam("ruc") String ruc,
            @RequestParam("codigoDespachador") String codigoDespachador,
            Model model) {

        int id = getAuthenticatedUserId();


        // Obtener el usuario logueado de la base de datos
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        // Actualizar los campos editables
        usuario.setRazonSocial(razonSocial);
        usuario.setRuc(ruc);
        usuario.setCodigoDespachador(codigoDespachador);

        // Cambiar el estado de postulación a '1'
        usuario.setIsPostulated((byte) 1);

        // Guardar los cambios en la base de datos
        usuarioRepository.save(usuario);

        // Redirigir a la página de inicio
        return "redirect:/usuario/inicio";
    }


    @GetMapping("/contraseña")
    public  String showpassword(Model model){
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "Usuario/password-usuario";
    }

    //Guardar cambios
    @PostMapping("cambiarContraseña")
    public String cambiarContra(@Valid PasswordChangeDto passwordChangeDto,
                                BindingResult result,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes,
                                Model model){

        //Validacion de errores
        if(result.hasErrors()){
            model.addAttribute("errors", result.getAllErrors());
            return "Usuario/password-usuario";
        }

        //Obtener el usuario autenticado desde el sistema de seguridad
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());

        //Vereficar si la contraseña actual ingresada coincide con la almacenada
        if(!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), usuario.getContrasena())){
            model.addAttribute("error", "La contraseña actual es incorrecta.");
            return "Usuario/password-usuario";
        }

        // Verificar si las contraseñas nuevas coinciden
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "Usuario/password-usuario";
        }

        // Actualizar la contraseña del usuario
        usuario.setContrasena(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        usuarioRepository.save(usuario);

        // Agregar mensaje de éxito a los flash attributes
        redirectAttributes.addFlashAttribute("exito", "Contraseña cambiada con éxito.");

        return "redirect:/usuario/inicio";
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
        int id = getAuthenticatedUserId();


        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

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
        int id = getAuthenticatedUserId();

        // Obtenemos los productos que el usuario ha recibido
        List<Producto> productosRecibidos = productosRepository.findProductosRecibidos(id);
        // Añadimos la lista de productos al modelo
        model.addAttribute("productosRecibidos", productosRecibidos);
        model.addAttribute("resena", new Resena()); // Agregamos un objeto vacío para el formulario

        return "Usuario/nuevaReseña-usuario";
    }

    @PostMapping("/guardarResenha")
    public String guardarResenha(
            @RequestParam("productoId") Integer productoId,
            @RequestParam("titulo") String titulo,
            @RequestParam("calificacion") Integer calificacion,
            @RequestParam("cuerpo") String cuerpo,
            @RequestParam("fueRapido") Byte fueRapido,
            @RequestParam("foto") MultipartFile foto) {

        int id = getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        Producto producto = productosRepository.findById(productoId).orElseThrow();

        Resena resena = new Resena();
        resena.setProductoIdproducto(producto);
        resena.setUsuarioIdusuario(usuario);
        resena.setTitulo(titulo);
        resena.setCalificacion(calificacion);
        resena.setCuerpo(cuerpo);
        resena.setFechaCreacion(LocalDate.now());
        resena.setFueRapido(fueRapido);

        // Guardar la reseña sin imagen para obtener el ID
        resena.setFoto("default.jpg");  // Colocamos un valor temporal por defecto
        resenaRepository.save(resena);

        // Ahora que la reseña tiene un ID, podemos guardar la imagen
        if (!foto.isEmpty()) {
            try {
                // Simplemente usar el ID como nombre de archivo sin preocuparse por la extensión
                String nombreArchivo = String.format("%06d", resena.getId()); // Solo el ID como nombre de archivo

                // Guardar el archivo
                storageService.guardarArchivo(foto, nombreArchivo);

                // Actualizar la reseña con el nombre del archivo de la foto
                resena.setFoto(nombreArchivo + ".jpg"); // Aquí puedes ajustar la extensión si es necesario
                resenaRepository.save(resena);  // Actualizar con el nombre de la foto
            } catch (IOException e) {
                e.printStackTrace();
                // Manejar error en guardar archivo
            }
        }


        return "redirect:/usuario/rese%C3%B1as";
    }

    private String getFileExtension(String fileName) {
        // Asegúrate de que devuelva solo la extensión correcta con el punto incluido
        return fileName.substring(fileName.lastIndexOf("."));
    }
    @GetMapping("/categoriaMujer")
    public String showMujerCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductRopaMujer(zonaId));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));

        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(1));
        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaHombre")
    public String showHombreCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductRopaHombre(zonaId));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(2));
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaTecnologia")
    public String showTecnologiaCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductElectronico(zonaId));
        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));
        return "Usuario/CategoriaTecnologia-usuario";
    }
    @GetMapping("/categoriaMuebles")
    public String showMuebleCategoria(Model model) {
        model.addAttribute("productList", productosRepository.findProductMuebles(zonaId));
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
        model.addAttribute("productList", productosRepository.findProductMueblesFilter(zonaId,categoria, material, marca, precioMin, precioMax));
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
        model.addAttribute("productList", productosRepository.findProductHombresFilter(zonaId,categoria, material,marca,color, precioMin, precioMax));
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
        model.addAttribute("productList", productosRepository.findProductMujerFilter(zonaId,categoria, talla,marca, color,precioMin, precioMax));
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
        model.addAttribute("productList", productosRepository.findProductElectroFilter(zonaId,categoria, almacenamiento,ram,marca,        precioMin, precioMax));
        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        return "Usuario/CategoriaTecnologia-usuario";
    }

    @GetMapping("/categoriaHombreSearch")
    public String showHombreCategoriaBuscar(Model model,


                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(zonaId,query,2));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaMujerSearch")
    public String showMujerCategoriaBuscar(Model model,
                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(zonaId,query,1));
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));
        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaTecnologiaSearch")
    public String showTecnologiaCategoriaBuscar(Model model,


                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(zonaId,query,3));
        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
        model.addAttribute("categoriasList",subCategoriaRepository.findSubcategorias(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaMuebleSearch")
    public String showMuebleCategoriaBuscar(Model model,


                                            @RequestParam(value = "query", required = false) String query) {
        model.addAttribute("productList", productosRepository.findProductQuery(zonaId,query,4));
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
                                   @RequestParam("LugarEntrega") String LugarEntrega,
                                   Model model,  RedirectAttributes attr) {

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
        pago.setId(listaPago.size() + 1);
        pagoRepository.save(pago);

        //Reducir la cantidad en la tienda por los productos ya pagado
        //Busco mi carrito actual
        Carrito miCarrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(myuser, (byte) 0);

        System.out.println("------------");
        System.out.println("carr: " + miCarrito.getIsDelete());
        List<ProductoEnCarrito> misProductos = productoEnCarritoRepository.findBycarritoIdcarrito(miCarrito);//la cantidad de cada producto está aqui

        if(misProductos.isEmpty()) {
            //No debería estar aquí
            attr.addFlashAttribute("error", "No hay productos en el carrito.");
        }else {
            System.out.println("-----------------");
            System.out.println("Aqui estoy");
            for(ProductoEnCarrito item : misProductos) {

                //Creamos un ProductoEnZonaId
                ProductoEnZonaId itemTienda = new ProductoEnZonaId();
                itemTienda.setProductoIdproducto(item.getProductoEnZona().getProductoIdproducto().getId());
                System.out.println("ID producto en tienda (zona):" + itemTienda.getProductoIdproducto());
                itemTienda.setZonaIdzona(myuser.getDistritoIddistrito().getZonaIdzona().getId());
                System.out.println("Id distrito: " + itemTienda.getZonaIdzona());

                //busco el producto en la tienda
                Optional<ProductoEnZona> tienda =productoEnZonaRepository.findById(itemTienda);
                System.out.println("-------");
                System.out.println("ID producto: " + item.getProductoEnZona().getProductoIdproducto().getId());

                //Obtengo el total de stock en tienda
                int Stock = tienda.get().getCantidad();
                int cantidadSelecionada = item.getCantidad();
                System.out.println("cantidad :" + cantidadSelecionada);

                int newTotal = Stock - cantidadSelecionada;

                System.out.println("Nuevo stock: " + newTotal);
                tienda.get().setCantidad(newTotal);
                productoEnZonaRepository.save(tienda.get());
                System.out.println("AYUDDDDDDDDDDDDDDDA");
            }
        }

        //Se genera una nueva orden
        Orden orden = new Orden();
        List<EstadoOrden> listaEstadoOrden = estadoOrdenRepository.findAll();
        EstadoOrden primerEstadoOrden = listaEstadoOrden.get(0);
        System.out.println("se creó una orden :DDDDDDDDDD");

        //Cambio al id de orden del carrito a null -> k
        Carrito carrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(myuser, (byte) 0 );


        //Generamos una nueva orden
        orden.setEstadoordenIdestadoorden(primerEstadoOrden);
        orden.setFechaCreacion(LocalDate.now());
        //orden.setFechaArribo(LocalDate.now().plusWeeks(3));
        orden.setIsDeleted((byte) 0);
        orden.setCodigo(RandomCodeGenerator.generateRandomCode(5));
        orden.setCostoTotal(monto);
        orden.setPagoIdpago(pago);
        orden.setUsuarioIdusuario(myuser);
        orden.setCarritoIdcarrito(carrito);
        orden.setLugarEntrega(LugarEntrega);


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
