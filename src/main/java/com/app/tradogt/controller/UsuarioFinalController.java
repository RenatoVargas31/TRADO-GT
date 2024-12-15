package com.app.tradogt.controller;


import com.app.tradogt.dto.OrdenCompraUserDto;
import com.app.tradogt.dto.PasswordChangeDto;
import com.app.tradogt.entity.*;
import com.app.tradogt.helpers.CreditCardValidator;
import com.app.tradogt.services.NotificationCorreoService;
import com.app.tradogt.services.NotificationService;
import com.app.tradogt.services.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.app.tradogt.repository.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;

import javax.management.Attribute;
import java.io.ByteArrayOutputStream;
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
    final DistritoRepository distritoRepository;


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

    @Autowired
    private NotificationCorreoService notificationCorreoService;

    @Autowired
    private NotificationService notificationService;

    final ProveedorRepository proveedorRepository;
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
    final MyFavoriteRepository myFavoriteRepository;



    public UsuarioFinalController(ProveedorRepository proveedorRepository,DistritoRepository distritoRepository, ProductosRepository productosRepository, OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, ProductoEnZonaRepository productoEnZonaRepository, PublicacionRepository publicacionRepository, ComentarioRepository comentarioRepository, SubCategoriaRepository subCategoriaRepository, CarritoRepository carritoRepository, PagoRepository pagoRepository, ResenaRepository resenaRepository, ProductoEnCarritoRepository productoEnCarritoRepository, AutenticacionRepository autenticacionRepository, EstadoOrdenRepository estadoOrdenRepository, MyFavoriteRepository myFavoriteRepository) {
        this.proveedorRepository = proveedorRepository;
        this.distritoRepository = distritoRepository;
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
        this.myFavoriteRepository = myFavoriteRepository;
    }

    public void updateOrderStatus() {
        //Obtener la fecha actual
        LocalDate today = LocalDate.now();
        Optional<EstadoOrden> estadoactual = estadoOrdenRepository.findById(3);
        Optional<EstadoOrden> arriboAlPais = estadoOrdenRepository.findById(4);
        Optional<EstadoOrden> aduanas = estadoOrdenRepository.findById(5);
        Optional<EstadoOrden> ruta = estadoOrdenRepository.findById(6);
        Optional<EstadoOrden> recibido = estadoOrdenRepository.findById(7);
        List<Orden> ordensInProcess = ordenRepository.findByEstadoordenIdestadoorden(estadoactual);
        for (Orden orden : ordensInProcess) {
            // Cambiar el estado de acuerdo a la fecha actual
            if (orden.getFechaArribo() != null && orden.getFechaArribo().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(arriboAlPais.get());
            } else if (orden.getFechaEnAduanas() != null && orden.getFechaEnAduanas().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(aduanas.get());
            } else if (orden.getFechaEnRuta() != null && orden.getFechaEnRuta().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(ruta.get());
            } else if (orden.getFechaRecibido() != null && orden.getFechaRecibido().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(recibido.get());
            }else{
                orden.setEstadoordenIdestadoorden(orden.getEstadoordenIdestadoorden());
            }
            // Guardar la orden actualizada
            ordenRepository.save(orden);
        }
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
    public String inicio(Model model) {
        updateOrderStatus();
        //Listar los pedidos recientes
        int userId = getAuthenticatedUserId();  // Obtener el ID del usuario autenticado
        Usuario user = usuarioRepository.findById(userId).get();
        int IdZona = user.getDistritoIddistrito().getZonaIdzona().getId();

        //Listar los productos con las calificaciones altas
        List<Object[]> productosTop = productoEnZonaRepository.productosTop(IdZona);
        model.addAttribute("productosTop", productosTop);

        //Listar mis ultimos pedidos
        List<Object[]> misUltimosPedidos = ordenRepository.misUltimosPedidos(userId);
        model.addAttribute("misUltimosPedidos", misUltimosPedidos);

        // Listar mis productos favoritos
        List<Object[]> misFavoritos = myFavoriteRepository.findFavorites(userId, IdZona);
        model.addAttribute("misFavoritos", misFavoritos);

        return "Usuario/inicio-usuario";
    }

    // Añadir favoritos
    @GetMapping("/favoritos")
    public String favoritos(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "query", defaultValue = "") String query,
            Model model) {

        int size=12;
        int userId = getAuthenticatedUserId(); // Obtener el ID del usuario autenticado
        Usuario user = usuarioRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        int IdZona = user.getDistritoIddistrito().getZonaIdzona().getId();

        // Crear el objeto Pageable
        Pageable pageable = PageRequest.of(page - 1, size);

        // Consultar los productos favoritos con paginación
        Page<Object[]> misFavoritosPage = myFavoriteRepository.findFavoritePageable(userId, IdZona,query, pageable);

        // Añadir los atributos al modelo
        model.addAttribute("misFavoritos", misFavoritosPage.getContent());
        model.addAttribute("currentPage", misFavoritosPage.getNumber() + 1);
        model.addAttribute("totalPages", misFavoritosPage.getTotalPages());
        model.addAttribute("totalElements", misFavoritosPage.getTotalElements());

        return "Usuario/favoritos-usuario";
    }


    // Borrar favoritos
    @PostMapping("/DeleteFavorito")
    String DeleteFavorito(Model model, @RequestParam("productId") Integer productId, RedirectAttributes attr) {

        //Buscar producto
        Optional<Producto> miProducto = productosRepository.findById(productId);
        //Obtener zona
        int idUser = getAuthenticatedUserId();
        Usuario user = usuarioRepository.findById(idUser).get();
        int idZona = user.getDistritoIddistrito().getZonaIdzona().getId();

        if(miProducto.isPresent()){
            //Buscamos el producto en la lista de favoritos
            MyFavoriteId myFavoriteId = new MyFavoriteId();
            myFavoriteId.setUsuarioIdusuario(idUser);
            myFavoriteId.setProductoIdproducto(productId);
            myFavoriteId.setZonaIdzona(idZona);
            // Bucamos si el producto se encuentra dentro de favoritos
            Optional<MyFavorite> productoPresenteEnFavoritos = myFavoriteRepository.findById(myFavoriteId);

            if(productoPresenteEnFavoritos.isPresent()){
                // Borramos de Favoritos
                myFavoriteRepository.deleteById(myFavoriteId);
                attr.addFlashAttribute("exito", "Producto eliminado de favoritos.");

            }else {
                attr.addFlashAttribute("error", "El producto no se encuentra en favoritos.");
            }

        }else{
            attr.addFlashAttribute("error", "Producto no encontrado.");
        }
        return "redirect:/usuario/productoDetalles?id=" + miProducto.get().getId();
    }

    @PostMapping("/guardarFavorito")
    public String guardarFavorito(Model model, @RequestParam("productId") Integer productId, RedirectAttributes attr){

        //Buscar producto
        Optional<Producto> miProducto = productosRepository.findById(productId);
        //Obtener zona
        int idUser = getAuthenticatedUserId();
        Usuario user = usuarioRepository.findById(idUser).get();
        int idZona = user.getDistritoIddistrito().getZonaIdzona().getId();
        if(miProducto.isPresent()){
            MyFavoriteId myFavoriteId = new MyFavoriteId();
            myFavoriteId.setUsuarioIdusuario(idUser);
            myFavoriteId.setProductoIdproducto(productId);
            myFavoriteId.setZonaIdzona(idZona);

            // Comprobar que el producto ha sido agregado por el usuario anteriormente
            Optional<MyFavorite> listarTodoProductoFavorito = myFavoriteRepository.findById(myFavoriteId);

            if(listarTodoProductoFavorito.isPresent()){
                // Error
                attr.addFlashAttribute("ProductoGuardadoError", "El producto "+ miProducto.get().getCodigo() + ' ' + miProducto.get().getNombre() + " ya ha sido agregado a Favoritos.");
            }else{
                // Se procede a guardar el producto
                MyFavorite myFavorite = new MyFavorite();
                myFavorite.setId(myFavoriteId);
                myFavorite.setUsuarioIdusuario(user);
                myFavorite.setFecha(LocalDate.now());
                myFavoriteRepository.save(myFavorite);

                attr.addFlashAttribute("ProductoGuardado","El producto "+ miProducto.get().getCodigo() + ' ' + miProducto.get().getNombre() + " se a agregado a Favoritos.");
            }
        }
        return "redirect:/usuario/productoDetalles?id=" + miProducto.get().getId();
    }

    @GetMapping("/libroReclamacion")
    public String libroReclamacion (Model model){

        int Iduser = getAuthenticatedUserId();
        Usuario user = usuarioRepository.findById(Iduser).get();
        // Obtener información del Usuario
        model.addAttribute("usuario", user);

        //Obtener lista de todo los Agentes de compra

        //Obtener lista de todo los productos del Usuario

        return "Usuario/libroReclamacion-usuario";
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
    @PostMapping("/deleteOrden")
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

    @GetMapping("/usuarioFoto/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getUsuarioFoto(@PathVariable("id") Integer id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

        if (usuarioOpt.isPresent() && usuarioOpt.get().getFoto() != null) {
            byte[] foto = usuarioOpt.get().getFoto();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Cambiar si usas otro formato
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        }

        // Retornar una imagen predeterminada si no hay foto
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/subirFoto")
    public String viewSubirFoto(@RequestParam("foto") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Por favor, selecciona una foto.");
            return "redirect:/usuario/perfil";
        }
/*
        try {
            // Ruta dinámica donde se guardarán las imágenes (fuera de static)
            String uploadDir = "uploads/fotosUsuarios/";

            // Crear el directorio si no existe
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Guardar el archivo en la ruta definida
            byte[] bytes = file.getBytes();
            Path path = uploadPath.resolve(file.getOriginalFilename());
            Files.write(path, bytes);

            // Obtener el usuario autenticado desde el modelo
            Usuario usuario = (Usuario) model.getAttribute("usuarioAutenticado");
            assert usuario != null;

            // Actualizar el nombre de la foto en la base de datos
            usuario.setFoto(file.getOriginalFilename());
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("message", "Foto subida satisfactoriamente: '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Error al cargar la foto '" + file.getOriginalFilename() + "'");
        }

 */
        try {
            // Obtener el usuario autenticado
            Usuario usuario = usuarioRepository.findById(getAuthenticatedUserId()).orElse(null);
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
                return "redirect:/usuario/perfil";
            }

            // Guardar la foto como byte array
            usuario.setFoto(file.getBytes());
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("success", "Foto de perfil actualizada con éxito.");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al subir la foto.");
        }


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
           }

        //Aqui haré el cambio de estado por fecha


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
           }

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
    public String showProductoDetalles(@RequestParam("id") int id,
                                       @RequestParam(value = "page", defaultValue = "1") int currentPage,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        // Buscar el producto por id
        int id2 = getAuthenticatedUserId();
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id2);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Optional<ProductoEnZona> productoEnZona = productoEnZonaRepository.findByIdAndZona(id, usuario.getDistritoIddistrito().getZonaIdzona().getId());

            model.addAttribute("PrecioEnvio", productoEnZona.get().getCostoEnvio());

            // Comprobar si el producto esta añadido a favoritos
            MyFavoriteId myFavoriteId = new MyFavoriteId();

            myFavoriteId.setUsuarioIdusuario(id2);
            myFavoriteId.setZonaIdzona(usuario.getDistritoIddistrito().getZonaIdzona().getId());
            myFavoriteId.setProductoIdproducto(id);

            Optional<MyFavorite> myFavorite = myFavoriteRepository.findById(myFavoriteId);

            if(myFavorite.isPresent()) {
                // Ya está añadido a favoritos
                model.addAttribute("MyFavorite", 1);

            }else {
                // No está añadido
                model.addAttribute("MyFavorite", 0);
            }




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
            model.addAttribute("product", producto);
            model.addAttribute("rating", resenaRepository.findRating(id));
            model.addAttribute("conteoRating", resenaRepository.countResena(id));
            model.addAttribute("currentId", id);

            List<Object[]> comentarios = resenaRepository.comentarioProducto(id);

            if (comentarios.isEmpty()) {
                model.addAttribute("alerta", "No hay comentarios para este producto.");
            } else {
                model.addAttribute("comentarios", comentarios);
            }

            // Configura la paginación (si es necesario)
            Pageable pageable = PageRequest.of(0, 4);
            Page<Producto> productoPage = null;

            // Según la categoría del producto, se obtienen productos relacionados
            switch (producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getId()) {
                case 1:
                    productoPage = productosRepository.findProductRopaMujer(zonaId, pageable);
                    break;
                case 2:
                    productoPage = productosRepository.findProductRopaHombre(zonaId, pageable);
                    break;
                case 3:
                    productoPage = productosRepository.findProductElectronico(zonaId, pageable);
                    break;
                case 4:
                    productoPage = productosRepository.findProductMuebles(zonaId, pageable);
                    break;
                default:
                    productoPage = productosRepository.findAll(pageable);
                    break;
            }

            // Si hay productos relacionados, se agregan al modelo
            if (productoPage != null) {
                model.addAttribute("productList", productoPage.getContent());
                model.addAttribute("currentPage", currentPage);
                model.addAttribute("sizeList", productoPage.getTotalElements());
                int totalPages = productoPage.getTotalPages();
                model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
            }

            // Mostrar aviso de la cantidad mínima de compra
            redirectAttributes.addFlashAttribute("MensajeAlerta", "La cantidad mínima de compra es de 12 productos");
            return "Usuario/producto-detalles";
        } else {
            return "redirect:/usuario/inicio"; // Si no se encuentra el producto, redirige a inicio
        }
    }


    //Seleción de producto al carrito
   @PostMapping("/selecionarProducto")
    public String selecionarProducto(@RequestParam("productoId") int productoId,
                                     @RequestParam("cantidad") String cantidadOculta , Model model, RedirectAttributes redirectAttributes) {

        //Busco un carrito creado a mi id
        int idUser= getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(idUser).get();
        int zonaid = usuario.getDistritoIddistrito().getZonaIdzona().getId();
       int cantidadP = Integer.parseInt(cantidadOculta);


           //Nombre del producto
           Producto producto = productosRepository.findById(productoId).get();

           Carrito hayCarrito = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario, (byte) 0);
           //Añadimos el producto al carrito nuevo
           ProductoEnCarrito misProductoEnCarrito = new ProductoEnCarrito();
           //Producto en Zona (TIENDA)
           Optional<ProductoEnZona> productoEnZona = productoEnZonaRepository.findByIdAndZona(productoId, usuario.getDistritoIddistrito().getZonaIdzona().getId());

           int stock = productoEnZona.get().getCantidad();

           ProductoEnCarritoId id_ProductoEnCarrito = new ProductoEnCarritoId();
           id_ProductoEnCarrito.setProductoenzonaProductoIdproducto(productoId);
           id_ProductoEnCarrito.setProductoenzonaZonaIdzona(zonaid);

           if(stock>=cantidadP && cantidadP>=12) {
               if (hayCarrito != null) {
                   //Hay carrito --> Añadimos el producto
                   Carrito miCarritoActual = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario, (byte) 0);

                   //Añado un nuevo producto
                   id_ProductoEnCarrito.setCarritoIdcarrito(miCarritoActual.getId());
                   //mensaje
                   redirectAttributes.addFlashAttribute("mensajeProductNuevo", "Se ha añadido un nuevo producto al carrito: " + producto.getCodigo() + ' ' + producto.getNombre());
                   misProductoEnCarrito.setCantidad(cantidadP);
                   misProductoEnCarrito.setCarritoIdcarrito(miCarritoActual);
                   misProductoEnCarrito.setId(id_ProductoEnCarrito);
                   misProductoEnCarrito.setProductoEnZona(productoEnZona.get());
                   productoEnCarritoRepository.save(misProductoEnCarrito);

               } else {
                   //No hay carrito --> Creamos uno nuevo
                   Carrito miCarritoNuevo = new Carrito();
                   miCarritoNuevo.setUsuarioIdusuario(usuario);
                   miCarritoNuevo.setIsDelete((byte) 0);
                   carritoRepository.save(miCarritoNuevo);
                   id_ProductoEnCarrito.setCarritoIdcarrito(miCarritoNuevo.getId());
                   //mensaje
                   redirectAttributes.addFlashAttribute("mensajeProductNuevo", "Se ha añadido un nuevo producto al carrito: " + producto.getCodigo() + ' ' + producto.getNombre());

                   misProductoEnCarrito.setCantidad(cantidadP);
                   misProductoEnCarrito.setCarritoIdcarrito(miCarritoNuevo);
                   misProductoEnCarrito.setId(id_ProductoEnCarrito);
                   misProductoEnCarrito.setProductoEnZona(productoEnZona.get());
                   productoEnCarritoRepository.save(misProductoEnCarrito);
               }
           } else if(cantidadP > stock ) {
               redirectAttributes.addFlashAttribute("MensajeAlerta", "No hay stock suficiente para este producto");
               return "redirect:/usuario/productoDetalles?id=" + productoId;
           }else if(cantidadP==12) {
               redirectAttributes.addFlashAttribute("mensajeProductNuevo", "Se ha añadido un nuevo producto al carrito: " + producto.getCodigo() + ' ' + producto.getNombre());
               return "redirect:/usuario/productoDetalles?id=" + productoId;
           } else if(cantidadP <12 ){
                   redirectAttributes.addFlashAttribute("MensajeAlerta", "Por favor, recuerda que la cantidad mínima para realizar una compra es de 12 productos. Ajusta tu pedido para continuar.");
                   return "redirect:/usuario/productoDetalles?id=" + productoId;
           }else {
               //mensaje de no hay stock suficiente
               redirectAttributes.addFlashAttribute("msgError", "No hay stock suficiente para este producto");
           }


       // Redirige al detalle del producto

       return "redirect:/usuario/productoDetalles?id=" + productoId;
    }

    @GetMapping("/carrito")
    public String showCarrito( Model model, RedirectAttributes attr) {

        //Usuario
        int user = getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(user).get();
        model.addAttribute("usuario", usuario);

        // Lista de los productos en carrito
        Carrito miCarrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(usuario, (byte) 0);
        List<ProductoEnCarrito> misProductos = productoEnCarritoRepository.findBycarritoIdcarrito( miCarrito);


        if (miCarrito != null) {
            model.addAttribute("carrito", misProductos);


            if(misProductos.isEmpty()) {
                model.addAttribute("mensaje", "El carrito se encuentra vacío.");
            }else {

                //Obtener la lista de los precios de envio del carrito
                BigDecimal sumaCostoEnvioTotal = BigDecimal.ZERO;
                for(ProductoEnCarrito productoEnCarrito : misProductos) {
                    BigDecimal costoDeEnvio = productoEnCarrito.getProductoEnZona().getCostoEnvio();
                    sumaCostoEnvioTotal = sumaCostoEnvioTotal.add(costoDeEnvio);
                }
                model.addAttribute("costoDeEnvio", sumaCostoEnvioTotal);
                miCarrito.setCostoEnvio(sumaCostoEnvioTotal);


            }

        }else {
            model.addAttribute("carrito", misProductos);
            model.addAttribute("mensaje", "El carrito se encuentra vacío");
        }

        return "Usuario/carrito-usuario";
    }
   @PostMapping("/actualizarCantidad")
    public String actualizarCantidad (RedirectAttributes attr,
            @RequestParam("total") BigDecimal total,
            @RequestParam("cantidad") List<Integer> cantidad) {

        // Obtener el usuario autenticado
        int id = getAuthenticatedUserId();
        Usuario usuario = usuarioRepository.findById(id).get();

        // Obtener el carrito del usuario
        Carrito micarrito = carritoRepository.findByUsuarioIdusuarioAndIsDelete(usuario,(byte) 0);

        // Si el carrito no existe
       if(micarrito == null) {
           return "redirect:/usuario/carrito";
       }

       // Validamos que todas las cantidades sean mayores o igual a 12
       for(Integer qty : cantidad) {
           if(qty < 12){
               attr.addFlashAttribute("ErrorCantidad", "La cantidad ingresada no es válida. El mínimo de compra es de 12 unidades. Por favor, inténtelo de nuevo.");

               return "redirect:/usuario/carrito";
           }
       }
        micarrito.setCostoTotal(total);
        carritoRepository.save(micarrito);
        List<ProductoEnCarrito> misproductos = productoEnCarritoRepository.findBycarritoIdcarrito(micarrito);
           for (int i = 0; i < misproductos.size(); i++) {
               // Actualiza la cantidad del producto
               misproductos.get(i).setCantidad(cantidad.get(i));
               // Guarda el producto actualizado en el repositorio
               productoEnCarritoRepository.save(misproductos.get(i));
           }
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

        ProductoEnCarritoId item = new ProductoEnCarritoId();
        item.setCarritoIdcarrito(miCarrito.getId());
        item.setProductoenzonaZonaIdzona(myUser.get().getDistritoIddistrito().getZonaIdzona().getId());
        item.setProductoenzonaProductoIdproducto(productoId);
        //Obtenego el producto
        Optional<ProductoEnCarrito> itemDelete = productoEnCarritoRepository.findById(item);
        productoEnCarritoRepository.delete(itemDelete.get());
        // Añade un mensaje de éxito
        redirectAttributes.addFlashAttribute("message", "El producto " + myProduct.get().getCodigo()+" ha sido eliminado de forma exitosa.");
        return "redirect:/usuario/carrito";
    }

    @GetMapping("/resenas")
    public String showResenhas(Model model) {
        List<Resena> resenas = resenaRepository
                .findByUsuarioIdusuarioIsAcceptedAndUsuarioIdusuarioIsPostulatedAndUsuarioIdusuarioIsActivated(1, 1, 1);

        List<Resena> listaResena = resenaRepository.findAll();

        // Si no hay resenas, puedes mostrar un mensaje
        if (listaResena.isEmpty()) {
            model.addAttribute("resenas", listaResena);
        }else {
            model.addAttribute("resenas", listaResena);
        }

        return "Usuario/resenas-usuario";
    }


    @GetMapping("/comunidad")
    public String showComunidad(Model model) {
        List<Object[]> publicaciones = publicacionRepository.findPublicacionesUsuariosNoBaneados();
        model.addAttribute("publicaciones", publicaciones);
        return "Usuario/comunidad-usuario";
    }

    @PostMapping("/comentario/{id}/like")
    public String likeComentario(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        // Buscar el comentario por su ID
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));

        // Incrementar el contador de likes
        comentario.setLikes(comentario.getLikes() + 1);

        // Guardar el comentario actualizado en la base de datos
        comentarioRepository.save(comentario);

        // Mensaje de éxito (opcional)
        redirectAttributes.addFlashAttribute("successMessage", "¡Like añadido al comentario!");

        // Redirigir a la página anterior (puedes personalizar esto según tu necesidad)
        return "redirect:/usuario/verPublicacion/" + comentario.getPublicacionIdpublicacion().getId();
    }

    @GetMapping("/foroProblema")
    public String showForoProblema() {
        return "Usuario/problema-soluciones";
    }

    @PostMapping("/publicacion/{id}/like")
    public String incrementarLike(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        // Buscar la publicación por su ID
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        // Incrementar el contador de likes
        publicacion.setLikes(publicacion.getLikes() + 1);

        // Guardar la publicación actualizada
        publicacionRepository.save(publicacion);

        // Añadir un mensaje de éxito
        redirectAttributes.addFlashAttribute("successMessage", "¡Te gustó esta publicación!");

        // Redirigir a la misma página
        return "redirect:/usuario/verPublicacion/" + id;
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
                                    RedirectAttributes redirectAttributes,
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
        redirectAttributes.addFlashAttribute("successMessage", "¡Comentario agregado exitosamente!");

        return "redirect:/usuario/verPublicacion/" + publicacionId;
    }



    @GetMapping("/detalleProblema")
    public String showDetalleProblema() {
        return "Usuario/viewProblema";
    }

    @GetMapping("/verresena/{id}")
    public String showDetalleResena(@PathVariable("id") Integer id, Model model) {
        // Buscar la resena por id
        Resena resena = resenaRepository.findById(id).orElse(null);

        // Si la resena no se encuentra, puedes redirigir a una página de error o similar
        if (resena == null) {
            return "redirect:/error";  // Página de error personalizada
        }

        // Pasar la resena al modelo para que esté disponible en la vista
        model.addAttribute("resena", resena);

        // Nombre de la vista Thymeleaf
        return "Usuario/verresena-usuario";
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
            RedirectAttributes redirectAttributes,
            Model model) {

        int id = getAuthenticatedUserId();

        // Obtener el usuario logueado de la base de datos
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();

        // Validaciones de los campos
        boolean hasErrors = false;

        // Validación de RUC
        if (ruc == null || !ruc.matches("\\d{10}")) {
            model.addAttribute("rucError", "El RUC debe contener exactamente 10 dígitos numéricos.");
            hasErrors = true;
        }

        // Validación de Razón Social
        if (razonSocial == null || razonSocial.trim().isEmpty() || !razonSocial.matches("[a-zA-Z0-9\\s]+")) {
            model.addAttribute("razonSocialError", "La razón social es obligatoria y solo puede contener letras, números y espacios.");
            hasErrors = true;
        }

        // Validación de Código de Despachador
        if (codigoDespachador == null || !codigoDespachador.matches("[A-Za-z0-9]+")) {
            model.addAttribute("codigoDespachadorError", "El código de despachador solo puede contener letras y números.");
            hasErrors = true;
        }

        // Si hay errores, regresar al formulario con los mensajes
        if (hasErrors) {
            model.addAttribute("usuario", usuario);
            return "Usuario/registroSolicitud"; // Vista del formulario
        }

        // Actualizar los campos editables
        usuario.setRazonSocial(razonSocial);
        usuario.setRuc(ruc);
        usuario.setCodigoDespachador(codigoDespachador);

        // Cambiar el estado de postulación a '1'
        usuario.setIsPostulated((byte) 1);

        // Guardar los cambios en la base de datos
        usuarioRepository.save(usuario);

        // Agregar un mensaje de éxito al Flash Attribute
        redirectAttributes.addFlashAttribute("successMessage", "¡Solicitud registrada con éxito!");

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

        notificationCorreoService.enviarCorreoCambioContraseña(usuario.getCorreo(),usuario.getNombre());
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
    public String crearPublicacion(@ModelAttribute("publicacion") Publicacion publicacion, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        int id = getAuthenticatedUserId();


        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Asignar el usuario a la publicación
        publicacion.setUsuarioIdusuario(usuario);

        // Asignar la fecha de creación automáticamente
        publicacion.setFechaCreacion(LocalDate.now());

        // Guardar la publicación en la base de datos
        publicacionRepository.save(publicacion);

        // Redirigir a la lista de publicaciones después de crear la nueva publicación
        redirectAttributes.addFlashAttribute("successMessage", "Publicación creada exitosamente.");

        return "redirect:/usuario/comunidad";
    }

    @GetMapping("/nuevaresena")
    public String nuevaResenha(@RequestParam(value = "productoId", required = false) Integer productoId, Model model) {
        int userId = getAuthenticatedUserId();

        // Obtenemos los IDs de los productos ya reseñados por el usuario
        List<Integer> productosResenhados = resenaRepository.findProductosResenhadosPorUsuario(userId);

        // Obtenemos los productos recibidos y excluimos los que ya fueron reseñados
        List<Producto> productosDisponibles = productosRepository.findProductosRecibidos(userId)
                .stream()
                .filter(producto -> !productosResenhados.contains(producto.getId()))
                .toList();

        // Añadimos los productos disponibles al modelo
        model.addAttribute("productosRecibidos", productosDisponibles);

        // Si se pasa un productoId, lo agregamos al modelo para seleccionar el producto automáticamente
        if (productoId != null) {
            model.addAttribute("productoSeleccionadoId", productoId);
        }

        model.addAttribute("resena", new Resena()); // Agregamos un objeto vacío para el formulario

        return "Usuario/pruebaTemp";
    }

    @GetMapping("/resenaFoto/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getResenaFoto(@PathVariable("id") Integer id) {
        Resena resena = resenaRepository.findById(id).orElse(null);

        if (resena != null && resena.getFoto() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Cambia según el tipo de imagen que uses
            return new ResponseEntity<>(resena.getFoto(), headers, HttpStatus.OK);
        }

        // Devuelve un estado 404 si la reseña o la foto no existen
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/proveedorImagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> obtenerImagenProveedor(@PathVariable Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        if (proveedor.getImagen() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(proveedor.getImagen(), headers, HttpStatus.OK);
    }


    @PostMapping("/guardarResenha")
    public String guardarResenha(
            @RequestParam("productoId") Integer productoId,
            @RequestParam("titulo") String titulo,
            @RequestParam("calificacion") Integer calificacion,
            @RequestParam("cuerpo") String cuerpo,
            @RequestParam("fueRapido") Byte fueRapido,
            @RequestParam("foto") MultipartFile foto,
            RedirectAttributes redirectAttributes) {

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
        // resena.setFoto("default.jpg"); // Valor inicial predeterminado
        resenaRepository.save(resena); // Guardar la reseña para obtener su ID

        try {
            if (foto != null && !foto.isEmpty()) {
                resena.setFoto(foto.getBytes());
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un error al procesar la imagen de la reseña.");
            return "redirect:/usuario/resenas";
        }

        // Guardar la reseña
        resenaRepository.save(resena);

        redirectAttributes.addFlashAttribute("message", "Reseña creada exitosamente.");


        return "redirect:/usuario/resenas";
    }


    private String getFileExtension(String fileName) {
        // Asegúrate de que devuelva solo la extensión correcta con el punto incluido
        return fileName.substring(fileName.lastIndexOf("."));
    }
    @GetMapping("/categoriaMujer")
    public String showMujerCategoria(Model model,
                                     @RequestParam(value = "page", required = false) String pageStr) {
        int currentPage = 1; // Valor por defecto

        // Si el parámetro 'page' no es un número entero, redirigimos sin él
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaMujer"; // Redirige sin el parámetro 'page'
        }

        // Si el 'page' es válido, entonces lo convertimos a entero
        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        // Verificar si el número de página es válido (mayor o igual a 1)
        currentPage = Math.max(currentPage, 1);

        // Calcular el número total de páginas
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository   .findProductRopaMujer(zonaId, pageable);



        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);


        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(1));

        return "Usuario/CategoriaMujer-usuario";
    }
    @GetMapping("/categoriaHombre")
    public String showHombreCategoria(Model model,
                                      @RequestParam(value = "page", required = false) String pageStr) {
        int currentPage = 1; // Valor por defecto

        // Si el parámetro 'page' no es un número entero, redirigimos sin él
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaHombre"; // Redirige sin el parámetro 'page'
        }

        // Si el 'page' es válido, entonces lo convertimos a entero
        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        // Verificar si el número de página es válido (mayor o igual a 1)
        currentPage = Math.max(currentPage, 1);

        // Calcular el número total de páginas
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductRopaHombre(zonaId, pageable);

        // Si la página está fuera de rango, redirigir a la primera página
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaHombre?page=1";
        }

        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages); // Total de páginas

        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(2));

        return "Usuario/CategoriaHombre-usuario";
    }

    @GetMapping("/categoriaTecnologia")
    public String showTecnologiaCategoria(Model model,
                                          @RequestParam(value = "page", required = false) String pageStr) {
        int currentPage = 1; // Valor por defecto

        // Si el parámetro 'page' no es un número entero, redirigimos sin él
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaTecnologia"; // Redirige sin el parámetro 'page'
        }

        // Si el 'page' es válido, entonces lo convertimos a entero
        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        // Verificar si el número de página es válido (mayor o igual a 1)
        currentPage = Math.max(currentPage, 1);

        // Calcular el número total de páginas
        Pageable pageable = PageRequest.of(currentPage - 1, 12      );
        Page<Producto> productoPage = productosRepository.findProductElectronico(zonaId, pageable);


        // Añadir atributos al modelo
        productList(model, currentPage, productoPage);
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(3));

        return "Usuario/CategoriaTecnologia-usuario";
    }


    @GetMapping("/categoriaMuebles")
    public String showMuebleCategoria(Model model,
                                      @RequestParam(value = "page", required = false) String pageStr) {
        int currentPage = 1; // Valor por defecto

        // Si el parámetro 'page' no es un número entero, redirigimos sin él
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaMuebles"; // Redirige sin el parámetro 'page'
        }

        // Si el 'page' es válido, entonces lo convertimos a entero
        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        // Verificar si el número de página es válido (mayor o igual a 1)
        currentPage = Math.max(currentPage, 1);

        // Calcular el número total de páginas
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductMuebles(zonaId, pageable);



        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages); // Total de páginas

        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(4));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(4));

        return "Usuario/CategoriaMuebles-usuario";
    }


    @GetMapping("/productoFoto/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductoFoto(@PathVariable("id") Integer id) {
        Optional<Producto> productoOpt = productosRepository.findById(id);

        if (productoOpt.isPresent() && productoOpt.get().getFoto() != null) {
            byte[] foto = productoOpt.get().getFoto();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Cambia según el tipo de imagen
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        }

        // Retorna una imagen predeterminada si no hay foto o el producto no existe
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/categoriaMueblesFilter")
    public String showMuebleCategoriaFilter(Model model,
                                            @RequestParam(value = "page", required = false) String pageStr,
                                            @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                            @RequestParam(value = "material", required = false) List<String> material,
                                            @RequestParam(value = "marca", required = false) List<String> marca,
                                            @RequestParam(value = "precioMin", required = false) Double precioMin,
                                            @RequestParam(value = "precioMax", required = false) Double precioMax) {
        int currentPage = 1; // Valor por defecto

        // Si el parámetro 'page' no es un número entero, redirigimos sin él
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaMueblesFilter"; // Redirige sin el parámetro 'page'
        }

        // Si el 'page' es válido, entonces lo convertimos a entero
        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        // Verificar si el número de página es válido (mayor o igual a 1)
        currentPage = Math.max(currentPage, 1);

        // Calcular el número total de páginas
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductMueblesFilter(zonaId, categoria, material, marca, precioMin, precioMax, pageable);

        // Si la página está fuera de rango, redirigir a la primera página
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaMueblesFilter?page=1";
        }

        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
        model.addAttribute("categoria", categoria);
        model.addAttribute("material", material);
        model.addAttribute("marca", marca);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(4));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(4));
        model.addAttribute("categoria", productosRepository.findDistinctMarca(4));

        return "Usuario/CategoriaMuebles-usuario";
    }

    @GetMapping("/categoriaHombreFilter")
    public String showHombreCategoriaFilter(Model model,
                                            @RequestParam(value = "page", required = false) String pageStr,
                                            @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                            @RequestParam(value = "material", required = false) List<String> material,
                                            @RequestParam(value = "color", required = false) List<String> color,
                                            @RequestParam(value = "marca", required = false) List<String> marca,
                                            @RequestParam(value = "precioMin", required = false) Double precioMin,
                                            @RequestParam(value = "precioMax", required = false) Double precioMax) {
        int currentPage = 1; // Valor por defecto

        // Validar el parámetro 'page'
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaHombreFilter"; // Redirige sin el parámetro 'page'
        }

        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        currentPage = Math.max(currentPage, 1);

        // Paginación
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductHombresFilter(
                zonaId, categoria, material, marca, color, precioMin, precioMax, pageable);

        // Verificar si la página solicitada es válida
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaHombreFilter?page=1";
        }

        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
        model.addAttribute("categoria", categoria);
        model.addAttribute("material", material);
        model.addAttribute("color", color);
        model.addAttribute("marca", marca);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);

        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));

        return "Usuario/CategoriaHombre-usuario";
    }




    @GetMapping("/categoriaMujerFilter")
    public String showMujerCategoriaFilter(Model model,
                                           @RequestParam(value = "page", required = false) String pageStr,
                                           @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                           @RequestParam(value = "talla", required = false) List<String> talla,
                                           @RequestParam(value = "color", required = false) List<String> color,
                                           @RequestParam(value = "marca", required = false) List<String> marca,
                                           @RequestParam(value = "precioMin", required = false) Double precioMin,
                                           @RequestParam(value = "precioMax", required = false) Double precioMax) {
        int currentPage = 1; // Valor por defecto

        // Validar el parámetro 'page'
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaMujerFilter"; // Redirige sin el parámetro 'page'
        }

        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        currentPage = Math.max(currentPage, 1);

        // Paginación
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductMujerFilter(
                zonaId, categoria, talla, marca, color, precioMin, precioMax, pageable);



        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
        model.addAttribute("categoria", categoria);
        model.addAttribute("talla", talla);
        model.addAttribute("color", color);
        model.addAttribute("marca", marca);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));
        return "Usuario/CategoriaMujer-usuario";
    }


    @GetMapping("/categoriaTecnologiaFilter")
    public String showTecnologiaCategoriaFilter(Model model,
                                                @RequestParam(value = "page", required = false) String pageStr,
                                                @RequestParam(value = "categoria", required = false) List<Integer> categoria,
                                                @RequestParam(value = "almacenamiento", required = false) List<String> almacenamiento,
                                                @RequestParam(value = "ram", required = false) List<String> ram,
                                                @RequestParam(value = "marca", required = false) List<String> marca,
                                                @RequestParam(value = "precioMin", required = false) Double precioMin,
                                                @RequestParam(value = "precioMax", required = false) Double precioMax) {
        int currentPage = 1; // Valor por defecto

        // Validar el parámetro 'page'
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaTecnologiaFilter"; // Redirige sin el parámetro 'page'
        }

        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        currentPage = Math.max(currentPage, 1);

        // Paginación
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductElectroFilter(
                zonaId, categoria, almacenamiento, ram, marca, precioMin, precioMax, pageable);

        // Verificar si la página solicitada es válida
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaTecnologiaFilter?page=1";
        }

        // Añadir atributos al modelo
        productList(model, currentPage, productoPage);
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));
        model.addAttribute("categoria", categoria);
        model.addAttribute("almacenamiento", almacenamiento);
        model.addAttribute("ram", ram);
        model.addAttribute("marca", marca);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        return "Usuario/CategoriaTecnologia-usuario";
    }

    private void productList(Model model, int currentPage, Page<Producto> productoPage) {
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);

        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
    }


    @GetMapping("/categoriaHombreSearch")
    public String showHombreCategoriaBuscar(Model model,
                                            @RequestParam(value = "page", required = false) String pageStr,
                                            @RequestParam(value = "query", required = false) String query) {
        int currentPage = 1; // Valor por defecto

        // Validar el parámetro 'page'
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaHombreSearch"; // Redirige sin el parámetro 'page'
        }

        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        currentPage = Math.max(currentPage, 1);

        // Paginación
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductQuery(zonaId, query, 2, pageable);

        // Verificar si la página solicitada es válida
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaHombreSearch?page=1";
        }

        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
        model.addAttribute("query", query);
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(2));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(2));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(2));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(2));

        return "Usuario/CategoriaHombre-usuario";
    }
    @GetMapping("/categoriaMujerSearch")
    public String showMujerCategoriaBuscar(Model model,
                                           @RequestParam(value = "page", required = false) String pageStr,
                                           @RequestParam(value = "query", required = false) String query) {
        int currentPage = 1; // Valor por defecto

        // Validar el parámetro 'page'
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaMujerSearch"; // Redirige sin el parámetro 'page'
        }

        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        currentPage = Math.max(currentPage, 1);

        // Paginación
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductQuery(zonaId, query, 1, pageable);

        // Verificar si la página solicitada es válida
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaMujerSearch?page=1";
        }

        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
        model.addAttribute("query", query);
        model.addAttribute("categoria", query);
        model.addAttribute("tallasList", productosRepository.findDistinctTallas(1));
        model.addAttribute("coloresList", productosRepository.findDistinctColores(1));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(1));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(1));

        return "Usuario/CategoriaMujer-usuario";
    }

    @GetMapping("/categoriaTecnologiaSearch")
    public String showTecnologiaCategoriaBuscar(Model model,
                                                @RequestParam(value = "page", required = false) String pageStr,
                                                @RequestParam(value = "query", required = false) String query) {
        int currentPage = 1; // Valor por defecto

        // Validar el parámetro 'page'
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaTecnologiaSearch"; // Redirige sin el parámetro 'page'
        }

        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        currentPage = Math.max(currentPage, 1);

        // Asigna un valor predeterminado (vacío) a 'query' si es null
        if (query == null) {
            query = "";
        }

        // Paginación
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductQuery(zonaId, query, 3, pageable);

        // Verificar si la página solicitada es válida
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaTecnologiaSearch?page=1";
        }

        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
        model.addAttribute("query", query);

        model.addAttribute("almacenamientoList", productosRepository.findDistinctAlmacenamiento(3));
        model.addAttribute("ramList", productosRepository.findDistinctRam(3));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(3));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(3));

        return "Usuario/CategoriaTecnologia-usuario";
    }


    @GetMapping("/categoriaMuebleSearch")
    public String showMuebleCategoriaBuscar(Model model,
                                            @RequestParam(value = "page", required = false) String pageStr,
                                            @RequestParam(value = "query", required = false) String query) {
        int currentPage = 1; // Valor por defecto

        // Validar el parámetro 'page'
        if (pageStr != null && !pageStr.matches("\\d+")) {
            return "redirect:/usuario/categoriaMuebleSearch"; // Redirige sin el parámetro 'page'
        }

        if (pageStr != null && pageStr.matches("\\d+")) {
            currentPage = Integer.parseInt(pageStr);
        }

        currentPage = Math.max(currentPage, 1);

        // Paginación
        Pageable pageable = PageRequest.of(currentPage - 1, 12);
        Page<Producto> productoPage = productosRepository.findProductQuery(zonaId, query, 4, pageable);

        // Verificar si la página solicitada es válida
        if (currentPage > productoPage.getTotalPages()) {
            return "redirect:/usuario/categoriaMuebleSearch?page=1";
        }

        // Añadir atributos al modelo
        model.addAttribute("productList", productoPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("sizeList", productoPage.getTotalElements()); // Total de productos
        int totalPages = productoPage.getTotalPages();
        model.addAttribute("partes", totalPages == 0 ? 1 : totalPages);
        model.addAttribute("query", query);
        model.addAttribute("materialesList", productosRepository.findDistinctMaterials(4));
        model.addAttribute("categoriasList", subCategoriaRepository.findSubcategorias(4));
        model.addAttribute("marcaList", productosRepository.findDistinctMarca(4));

        return "Usuario/CategoriaMuebles-usuario";
    }

    @GetMapping("/checkout-info")
    public String showcheckout(Model model, RedirectAttributes Attr) {

        int user = getAuthenticatedUserId();
        Usuario myuser = usuarioRepository.findByIdUsuario(user);

        model.addAttribute("usuario", myuser);

        //Ingresar los datos de nuestro carrito actual
        Carrito miCarrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(myuser, (byte) 0);
        BigDecimal total = miCarrito.getCostoTotal();
        BigDecimal costoEnvio = miCarrito.getCostoEnvio();
        model.addAttribute("total", total); //Se obtiene el costo total

        if (miCarrito != null) {
            List<ProductoEnCarrito> misProductos = productoEnCarritoRepository.findBycarritoIdcarrito( miCarrito);

            if(misProductos.isEmpty()) {
                Attr.addAttribute("mensajeCarritoEmpty", "El carrito se encuentra vacío");
            }else {
                model.addAttribute("carrito", misProductos);

                model.addAttribute("distritos", distritoRepository.findAll());
                model.addAttribute("usuario", usuarioRepository.findByIdUsuario(user));
                model.addAttribute("costoEnvio", costoEnvio);
            }

        }else {
            Attr.addAttribute("mensajeCarritoEmpty", "El carrito se encuentra vacío");
            return "Usuario/carrito-usuario";
        }
        return "Usuario/billing-info-usuario";
    }

    @GetMapping("/notificaciones")
    @ResponseBody
    public List<Map<String, String>> obtenerNotificaciones(@RequestParam("usuarioId") int usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener las notificaciones no leídas
        List<Notificacion> notificaciones = notificationService.getUnreadNotifications(usuario);

        // Transformar las notificaciones en una estructura enriquecida con URLs
        return notificaciones.stream().map(notif -> {
            Map<String, String> data = new HashMap<>();
            data.put("contenido", notif.getContenido());
            data.put("orderId", notif.getOrden().getCodigo());
            data.put("imageUrl", getImageUrlForOrder(notif.getOrden())); // Generar la URL de la imagen
            data.put("idImportador",notif.getOrden().getUsuarioIdusuario().getId().toString());
            return data;
        }).collect(Collectors.toList());
    }

    // Método auxiliar para obtener la URL de la imagen según el estado de la orden
    private String getImageUrlForOrder(Orden orden) {
        if (orden == null || orden.getEstadoordenIdestadoorden() == null) {
            return "/images/notiIcons/default-notification.svg";
        }

        switch (orden.getEstadoordenIdestadoorden().getId()) {
            case 4: return "/images/notiIcons/orden-arriboPais-svgrepo-com.svg";
            case 5: return "/images/notiIcons/orden-aduanas-svgrepo-com.svg";
            case 6: return "/images/notiIcons/orden-enCamino-svgrepo-com.svg";
            case 7: return "/images/notiIcons/orden-recibida-svgrepo-com.svg";
            default: return "/images/notiIcons/order-svgrepo-com.svg";
        }
    }

    @GetMapping("notificacionesTotales")
    public String allNotifications(Model model) {
        return "Usuario/allNotifications-usuario";
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
                                   Model model, RedirectAttributes attr) {

        // Validar número de tarjeta y código CVV
        if (!CreditCardValidator.isValidCardNumber(numeroTarjeta, metodo)) {
            attr.addFlashAttribute("error", "Número de tarjeta inválido.");

            System.out.println("El errror esta aqui :D");
            return "redirect:/usuario/checkout-info";
        }

        if (!CreditCardValidator.isValidCVV(codigoCVV, metodo)) {
            attr.addFlashAttribute("error", "Código CVV inválido.");
            System.out.println("El errror esta aqui D:");

            return "redirect:/usuario/checkout-info";
        }

        if (!CreditCardValidator.isValidExpiryDate(fechaTarjeta)) {
            attr.addFlashAttribute("error", "Fecha de tarjeta inválida.");
            System.out.println("El errror esta aqui D':");
            return "redirect:/usuario/checkout-info";
        }


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
        List<Pago> listaPago = pagoRepository.findAll();
        pago.setId(listaPago.size() + 1);
        pagoRepository.save(pago);

        // Busco mi carrito actual
        Carrito miCarrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(myuser, (byte) 0);
        List<ProductoEnCarrito> misProductos = productoEnCarritoRepository.findBycarritoIdcarrito(miCarrito);

        int salvavida = 0;
        ProductoEnCarrito productoError = new ProductoEnCarrito();

        if (misProductos.isEmpty()) {
            attr.addFlashAttribute("error", "No hay productos en el carrito.");
            return "redirect:/usuario/carrito";
        } else {
            for (ProductoEnCarrito item : misProductos) {
                ProductoEnZonaId itemTienda = new ProductoEnZonaId();
                itemTienda.setProductoIdproducto(item.getProductoEnZona().getProductoIdproducto().getId());
                itemTienda.setZonaIdzona(myuser.getDistritoIddistrito().getZonaIdzona().getId());

                Optional<ProductoEnZona> tienda = productoEnZonaRepository.findById(itemTienda);
                if (!tienda.isPresent()) {
                    attr.addFlashAttribute("error", "El producto no se encontró en la tienda.");
                    return "redirect:/usuario/carrito";
                }

                int i = tienda.get().getContar();
                tienda.get().setContar(i + 1);

                int Stock = tienda.get().getCantidad();
                int cantidadSelecionada = item.getCantidad();
                int newTotal = Stock - cantidadSelecionada;
                salvavida = newTotal;
                productoError = item;

                if (newTotal < 0) {
                    break;
                } else {
                    if (newTotal < 25) {
                        tienda.get().setEstadoRepo((byte) 1);
                        String mensajeNotificacion = "El stock del producto '" + tienda.get().getProductoIdproducto().getNombre() +
                                "' es bajo. Cantidad actual: " + newTotal;
                        notificationService.stockNotification(mensajeNotificacion, tienda.get().getZonaIdzona());
                    }
                    tienda.get().setCantidad(newTotal);
                    productoEnZonaRepository.save(tienda.get());
                }
            }
        }

        if (salvavida < 0) {
            attr.addFlashAttribute("error", "No hay stock suficiente para el producto " +
                    productoError.getProductoEnZona().getProductoIdproducto().getNombre() +
                    ". Stock actual: " +
                    productoError.getProductoEnZona().getCantidad());

            return "redirect:/usuario/carrito";
        } else {
            Orden orden = new Orden();
            List<EstadoOrden> listaEstadoOrden = estadoOrdenRepository.findAll();
            EstadoOrden primerEstadoOrden = listaEstadoOrden.get(0);

            Carrito carrito = carritoRepository.findByusuarioIdusuarioAndIsDelete(myuser, (byte) 0);

            orden.setEstadoordenIdestadoorden(primerEstadoOrden);
            orden.setFechaCreacion(LocalDate.now());
            orden.setIsDeleted((byte) 0);
            orden.setCodigo(RandomCodeGenerator.generateRandomCode(5));
            orden.setCostoTotal(monto);
            orden.setPagoIdpago(pago);
            orden.setUsuarioIdusuario(myuser);
            orden.setCarritoIdcarrito(carrito);
            orden.setLugarEntrega(LugarEntrega);

            ordenRepository.save(orden);
            Integer idOrden = orden.getId();
            attr.addFlashAttribute("exito", "Se ha guardado de forma exitosa la orden " + orden.getCodigo() + " .");
            model.addAttribute("orden", orden);

            if (carrito != null) {
                carrito.setIsDelete((byte) 1);
                carritoRepository.save(carrito);
            }

            return "redirect:/usuario/misPedidos";
        }
    }


}
