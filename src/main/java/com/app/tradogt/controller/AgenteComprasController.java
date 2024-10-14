package com.app.tradogt.controller;

import com.app.tradogt.dto.*;
import com.app.tradogt.entity.Distrito;
import com.app.tradogt.entity.EstadoOrden;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agente")
public class AgenteComprasController {

    final UsuarioRepository usuarioRepository;
    final OrdenRepository ordenRepository;
    private final ProveedorRepository proveedorRepository;
    final ProductosRepository productosRepository;
    final EstadoOrdenRepository estadoOrdenRepository;
    @Autowired
    private DistritoRepository distritoRepository;

    public AgenteComprasController(UsuarioRepository usuarioRepository, OrdenRepository ordenRepository, ProveedorRepository proveedorRepository, ProductosRepository productosRepository, EstadoOrdenRepository estadoOrdenRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ordenRepository = ordenRepository;
        this.proveedorRepository = proveedorRepository;
        this.productosRepository = productosRepository;
        this.estadoOrdenRepository = estadoOrdenRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método auxiliar para obtener el ID del usuario autenticado
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
    public String showInicio() {
        return "Agente/inicio-Agente";
    }

    @GetMapping("/contraseña")
    public String changePass(Model model){
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "Agente/changePass-Agente";
    }

    @PostMapping("/cambiarContrasena")
    public String cambiarContrasena(@Valid PasswordChangeDto passwordChangeDto,
                                    BindingResult result,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        // Validación de errores
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "Agente/changePass-Agente";  // Retorna a la vista con los errores
        }

        // Obtener el usuario autenticado desde el sistema de seguridad
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());

        // Verificar si la contraseña actual ingresada coincide con la almacenada
        if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), usuario.getContrasena())) {
            model.addAttribute("error", "La contraseña actual es incorrecta.");
            return "Agente/changePass-Agente";  // Retorna a la vista con el mensaje de error
        }

        // Verificar si las contraseñas nuevas coinciden
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "Agente/changePass-Agente";  // Retorna a la vista con el mensaje de error
        }

        // Actualizar la contraseña del usuario
        usuario.setContrasena(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        usuarioRepository.save(usuario);  // Guardar los cambios en la base de datos

        // Agregar mensaje de éxito a los flash attributes
        redirectAttributes.addFlashAttribute("exito", "Contraseña cambiada con éxito.");
        return "redirect:/agente/perfil";  // Redirige a la página del perfil
    }


    //TABLEROS DE ÓRDENES
    @GetMapping("/allOrders")
    public String showAllOrders(Model model) {
        int idAgente = getAuthenticatedUserId();  // Obtener el ID del usuario autenticado

        // Obtener los resultados de la consulta nativa
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNative(idAgente);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (BigDecimal) result[3],  // montoTotal
                        (String) result[4],  // codigoOrden
                        (Long) result[5],  //idAgenteCompra
                        (String) result[6], //idEstadoPedido
                        (Integer) result[7] //idOrden
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/allOrdersTable-Agente";
    }
    @GetMapping("/sinAsignarOrders")
    public String showSinAsignarOrders(Model model) {
        // Obtener los resultados de la consulta nativa
        //Por el momento yo mismo le asigno el id que usará para buscar las órdenes asignadas
        //pues este id será proporcionado recién automáticamente cuando se realice el LOGIN
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeSinAsignar();

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (BigDecimal) result[3],  // montoTotal
                        (String) result[4],  // codigoOrden
                        (Long) result[5],  //idAgenteCompra
                        (String) result[6], //idEstadoPedido
                        (Integer) result[7] //idOrden
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);

        return "Agente/sinAsignarOrdersTable-Agente";
    }
    @GetMapping("/pendingOrders")
    public String showPendingOrders(Model model) {
        int idAgente = getAuthenticatedUserId();  // Obtener el ID del usuario autenticado
        // Obtener los resultados de la consulta nativa

        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativePendiente(idAgente);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (BigDecimal) result[3],  // montoTotal
                        (String) result[4],  // codigoOrden
                        (Long) result[5],  //idAgenteCompra
                        (String) result[6], //idEstadoPedido
                        (Integer) result[7] //idOrden
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/pendingOrdersTable-Agente";
    }
    @GetMapping("/enProcesoOrders")
    public String showEnProcesoOrders(Model model) {
        int idAgente = getAuthenticatedUserId();  // Obtener el ID del usuario autenticado
        // Obtener los resultados de la consulta nativa

        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeEnProceso(idAgente);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (BigDecimal) result[3],  // montoTotal
                        (String) result[4],  // codigoOrden
                        (Long) result[5],  //idAgenteCompra
                        (String) result[6], //idEstadoPedido
                        (Integer) result[7] //idOrden
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/procesoOrdersTable-Agente";
    }
    @GetMapping("/solveOrders")
    public String showSolveOrders(Model model) {
        int idAgente = getAuthenticatedUserId();  // Obtener el ID del usuario autenticado
        // Obtener los resultados de la consulta nativa

        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeResuelto(idAgente);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (BigDecimal) result[3],  // montoTotal
                        (String) result[4],  // codigoOrden
                        (Long) result[5],  //idAgenteCompra
                        (String) result[6], //idEstadoPedido
                        (Integer) result[7] //idOrden
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/solveOrdersTable-Agente";
    }
    @GetMapping("/perfil")
    public String showPerfil(Model model) {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;

        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername(); // Obtener el correo del usuario autenticado
        } else {
            correoUsuario = principal.toString();
        }

        // Buscar el usuario en la base de datos usando el correo
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);  // Suponiendo que tienes un método para buscar por correo

        // Pasar la información del usuario al modelo para que esté disponible en la vista
        model.addAttribute("usuario", usuario);

        return "Agente/profile-Agente";  // Retorna la vista del perfil del agente
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


    //Información de órdenes de usuarios asignados
    //OJO: Se crean múltiples vistas de estos detalles con el objetivo de mostrar el html
    //mas una vez se defina en el backend la lógica solo será necesario una vista que
    //varíe según corresponda el estado
    /*
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
    */

    //Información de órdenes de usuarios asignados (este es el definitivo xD)
    @GetMapping("/detailsOrder")
    public String showDetails(Model model, @RequestParam ("idOrden") int idOrden,
                                           @RequestParam ("idUsuario") int idUsuario){
        int idAgente = getAuthenticatedUserId();
        model.addAttribute("idAgente",idAgente);

        Optional<Orden> optionalOrden = ordenRepository.findById(idOrden);

         if(optionalOrden.isPresent()){

             Orden orden = optionalOrden.get();
             model.addAttribute("orden",orden);
             //Info de usuario que vemos en detalles de ordenes
             Usuario usuario = usuarioRepository.findById(idUsuario).get();
             model.addAttribute("usuario",usuario);
             //Info del proovedor de la orden
             List<Object[]> resultados = proveedorRepository.findProveedorByOrderId(idOrden);
             List<ProveedorInfoAgtDto> listaProveedores = new ArrayList<>();
             BigDecimal subtotal = BigDecimal.ZERO;  // Inicializar el subtotal
             BigDecimal totalEnvio = BigDecimal.ZERO;  // Inicializar el costo de envío

             for (Object[] fila : resultados) {
                 ProveedorInfoAgtDto dto = new ProveedorInfoAgtDto();
                 dto.setIdProveedor((Integer) fila[0]);
                 dto.setNombreProvedor((String) fila[1]);
                 dto.setTelefonoProovedor((String) fila[2]);
                 dto.setNombreTienda((String) fila[3]);

                 listaProveedores.add(dto);
             }
             List<ProveedorInfoAgtDto> listaProov = listaProveedores;
             model.addAttribute("listaProv",listaProov);


             //Info de los productos presentes en la orden
             List<Object[]> resultados2 = productosRepository.findProductDetailsByOrderId(idOrden);

             List<ProductDetailsAgtDto> listaProductos = new ArrayList<>();

             for (Object[] fila : resultados2) {
                 ProductDetailsAgtDto dto = new ProductDetailsAgtDto();
                 dto.setIdProducto((Integer) fila[0]);  // Mapea el idProducto
                 dto.setNombreProducto((String) fila[1]);
                 // Convertir el precio a BigDecimal si no lo es
                 BigDecimal precioProducto = new BigDecimal(fila[2].toString());
                 dto.setPrecioProducto(precioProducto);
                 dto.setCantidadProducto((Integer) fila[3]);
                 dto.setCostoEnvio((BigDecimal) fila[4]);

                 // Sumar los precios y los costos de envío
                 subtotal = subtotal.add(dto.getPrecioProducto().multiply(BigDecimal.valueOf(dto.getCantidadProducto())));
                 totalEnvio = totalEnvio.add(dto.getCostoEnvio());

                 listaProductos.add(dto);
             }
             model.addAttribute("listaProductos",listaProductos);

             BigDecimal total = subtotal.add(totalEnvio);

             model.addAttribute("listaProductos", listaProductos);
             model.addAttribute("subtotal", subtotal);  // Pasar el subtotal a la vista
             model.addAttribute("totalEnvio", totalEnvio);  // Pasar el costo de envío a la vista
             model.addAttribute("total", total);  // Pasar el total a la vista

             return "Agente/detailsOrder-Agente";
         } else{
             return "redirect:/agente/allOrders";
         }

    }
    // Método para eliminar la orden (cambio lógico de isDeleted)
    @PostMapping("/changeToEnProceso")
    public String changeStateInProcess(@RequestParam("idOrden") int idOrden, RedirectAttributes redirectAttributes) {

        // Buscamos la orden por ID
        Optional<Orden> optionalOrden = ordenRepository.findById(idOrden);

        if (optionalOrden.isPresent()) {
            Orden orden = optionalOrden.get();
            EstadoOrden estadoOrden = estadoOrdenRepository.findById(3).get();
            // Cambiamos el estado de la orden a estado 3 (En proceso)
            orden.setEstadoordenIdestadoorden(estadoOrden);
            // Guardar los cambios en la base de datos
            ordenRepository.save(orden);
            // Agregar un mensaje flash para la confirmación
            redirectAttributes.addFlashAttribute("mensaje", "La orden ha sido validada con éxito.");
        }else{
            // Si no se encuentra la orden
            redirectAttributes.addFlashAttribute("error", "La orden no se encontró.");
        }

        // Redirigimos a la página de listado de órdenes después de eliminar
        return "redirect:/agente/enProcesoOrders";
    }

    // Método para eliminar la orden (cambio lógico de isDeleted)
    @PostMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("idOrden") int idOrden, RedirectAttributes redirectAttributes) {

        // Buscamos la orden por ID
        Optional<Orden> optionalOrden = ordenRepository.findById(idOrden);

        if (optionalOrden.isPresent()) {
            Orden orden = optionalOrden.get();
            // Cambiamos el campo isDeleted a 1
            orden.setIsDeleted((byte) 1);
            ordenRepository.save(orden);
            // Agregar un mensaje flash para la confirmación
            redirectAttributes.addFlashAttribute("mensaje", "La orden ha sido eliminada con éxito.");
        }else{
            // Si no se encuentra la orden
            redirectAttributes.addFlashAttribute("mensaje", "La orden no se encontró.");
        }

        // Redirigimos a la página de listado de órdenes después de eliminar
        return "redirect:/agente/allOrders";
    }

    //Tomado de órdenes
    // Método para manejar la solicitud POST
    @PostMapping("/tomarOrden")
    public String takeOrder(
            @RequestParam("idOrden") int idOrden,
            @RequestParam("idAgente") int idAgente,
            RedirectAttributes redirectAttributes) {

        // Buscamos la orden por su ID
        Optional<Orden> optionalOrden = ordenRepository.findById(idOrden);

        if (optionalOrden.isPresent()) {
            Orden orden = optionalOrden.get();
            EstadoOrden estadoOrden = estadoOrdenRepository.findById(2).get();
            Usuario agente = usuarioRepository.findById(idAgente).get();
            // Cambiamos el estado de la orden a estado 2 (En validación)
            orden.setEstadoordenIdestadoorden(estadoOrden);
            // Asignamos el id del agente que toma la orden
            orden.setAgentcompraIdusuario(agente);
            // Guardar los cambios en la base de datos
            ordenRepository.save(orden);

            // Agregar mensaje de éxito al flash attributes
            redirectAttributes.addFlashAttribute("msgOrden", "La orden ha sido asignada con éxito.");
        } else {
            // Si no se encuentra el usuario
            redirectAttributes.addFlashAttribute("errorOrden", "La orden no fue encontrada.");
        }

        // Redirigir a la página de usuarios baneados
        return "redirect:/agente/pendingOrders";
    }

    //Cambiar la dirección de la orden
    @PostMapping("/updateDelivery")
    public String changeDireccion(
            @RequestParam("idOrden") int idOrden,
            @RequestParam("idUsuario") int idUsuario,
            @RequestParam("direccion")
            @NotBlank(message = "La dirección de entrega es obligatoria.")
            @Size(min = 10, max = 100, message = "La dirección debe tener entre 10 y 100 caracteres.")
            String direccionEntrega,
            RedirectAttributes redirectAttributes){

        // Buscamos la orden por su ID
        Optional<Orden> optionalOrden = ordenRepository.findById(idOrden);

        if (optionalOrden.isPresent()) {
            Orden orden = optionalOrden.get();
            // Cambiamos la dirección de entrega de la orden a la nueva
            orden.setLugarEntrega(direccionEntrega);
            // Guardar los cambios en la base de datos
            ordenRepository.save(orden);

            // Agregar mensaje de éxito al flash attributes
            redirectAttributes.addFlashAttribute("success", "La dirección de entrega ha sido modificada con éxito.");
        } else {
            // Si no se encuentra el usuario
            redirectAttributes.addFlashAttribute("error", "La orden no fue encontrada.");
        }

        // Redirigir a la página de usuarios baneados
        return "redirect:/agente/detailsOrder?idOrden=" + idOrden + "&idUsuario=" + idUsuario;
    }


    //Tableros de USUARIOS

    @GetMapping("/allUsers")
    public String showAllUsers(Model model) {
        int idAgente = getAuthenticatedUserId();  // Obtener el ID del usuario autenticado

        // Realizar la consulta nativa para obtener los usuarios únicos asignados a las órdenes del agente
        List<Object[]> usuariosDatos = usuarioRepository.findUniqueUsersByAgent(idAgente);

        // Crear una lista de usuarios formateada para pasarla a la vista
        List<Usuario> usuarioList = new ArrayList<>();

        // Procesar los datos recibidos para construir los objetos Usuario
        for (Object[] usuarioData : usuariosDatos) {

            Usuario usuario = new Usuario();
            usuario.setDni((String) usuarioData[0]);  // DNI
            usuario.setNombre((String) usuarioData[1]);  // Nombre
            usuario.setApellido((String) usuarioData[2]);  // Apellido
            Distrito distrito = distritoRepository.findByNombre(String.valueOf(usuarioData[3])).get();
            usuario.setDistritoIddistrito(distrito) ;
            usuario.setCorreo((String) usuarioData[4]);  // Correo
            usuario.setId((Integer) usuarioData[5]);  // ID del usuario


            // Añadir al Set para evitar duplicados
            usuarioList.add(usuario);
        }

        // Pasar los datos de usuarios únicos al modelo
        model.addAttribute("usuarioList", usuarioList);

        return "Agente/allUsersTable-Agente";
    }

    @GetMapping("/habilitadosUsers")
    public String showHabilitadosUsers(Model model) {
        model.addAttribute("usuarioList", usuarioRepository.findByIsActivatedAndAgentcompraIdusuario_Id((byte) 1,4));
        return "Agente/habilitadosUsersTable-Agente";
    }
    @GetMapping("/baneadosUsers")
    public String showBaneadosUsers(Model model) {
        model.addAttribute("usuarioList", usuarioRepository.findByIsActivatedAndAgentcompraIdusuario_Id((byte) 0,4));
        return "Agente/baneadosUsersTable-Agente";
    }

    //Información sobre usuarios asignados a agentes
    @GetMapping("/infoUsuario")
    public String showInfoUser(Model model, @RequestParam("idUsuario") Integer idUsuario){
        model.addAttribute("usuario", usuarioRepository.findById(idUsuario).get());

        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeParUsuario(4,idUsuario);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (BigDecimal) result[3],  // montoTotal
                        (String) result[4],  // codigoOrden
                        (Long) result[5],  //idAgenteCompra
                        (String) result[6], //idEstadoPedido
                        (Integer) result[7] //idOrden
                ))
                .collect(Collectors.toList());

        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/detallesUsuarios-Agente";
    }

    //Baneo de importadores
    // Método para manejar la solicitud POST del formulario de baneo
    @PostMapping("/banearImportador")
    public String banImportador(
            @Valid BanUserDto banUserDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Verificar si hay errores de validación
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en los datos ingresados: " + result.getFieldError().getDefaultMessage());
            return "redirect:/agente/allUsers";
        }

        // Buscamos al importador por su ID
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(banUserDTO.getIdImportador());

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Cambiar el estado del usuario a baneado (isActivated = 0)
            usuario.setIsActivated((byte) 0);
            // Establecer el motivo del baneo
            usuario.setMotivoBaneo(banUserDTO.getBanReason());
            // Guardar los cambios en la base de datos
            usuarioRepository.save(usuario);

            // Buscamos todas las órdenes que tengan asignado el id del usuario
            List<Orden> listaOrdenesAsignadas = ordenRepository.findAllByUsuarioIdusuario(usuario);

            // Marcamos cada orden como eliminada (isDeleted = 1)
            for (Orden orden : listaOrdenesAsignadas) {
                orden.setIsDeleted((byte) 1);  // Cambiamos el valor de isDeleted a 1
                ordenRepository.save(orden);   // Guardamos los cambios en la base de datos
            }

            // Agregar mensaje de éxito al flash attributes
            redirectAttributes.addFlashAttribute("mensaje", "El usuario ha sido baneado con éxito.");
        } else {
            // Si no se encuentra el usuario
            redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado.");
        }

        // Redirigir a la página de usuarios
        return "redirect:/agente/allUsers";
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
