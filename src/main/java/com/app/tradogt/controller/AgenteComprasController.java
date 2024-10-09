package com.app.tradogt.controller;

import com.app.tradogt.dto.OrdenCompraAgtDto;
import com.app.tradogt.dto.ProductDetailsAgtDto;
import com.app.tradogt.dto.ProveedorInfoAgtDto;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.repository.OrdenRepository;
import com.app.tradogt.repository.ProductosRepository;
import com.app.tradogt.repository.ProveedorRepository;
import com.app.tradogt.repository.UsuarioRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agente")
public class AgenteComprasController {

    final UsuarioRepository usuarioRepository;
    final OrdenRepository ordenRepository;
    private final ProveedorRepository proveedorRepository;
    final ProductosRepository productosRepository;

    public AgenteComprasController(UsuarioRepository usuarioRepository, OrdenRepository ordenRepository, ProveedorRepository proveedorRepository, ProductosRepository productosRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ordenRepository = ordenRepository;
        this.proveedorRepository = proveedorRepository;
        this.productosRepository = productosRepository;
    }

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
    public String changePass(){
        return "Agente/changePass-Agente";
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
                        (String) result[3],  // metodoPago
                        (String) result[4],  // agenteCompra
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
                        (String) result[3],  // metodoPago
                        (String) result[4],  // agenteCompra
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
        // Obtener los resultados de la consulta nativa
        //Por el momento yo mismo le asigno el id que usará para buscar las órdenes asignadas
        //pues este id será proporcionado recién automáticamente cuando se realice el LOGIN
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativePendiente(4);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (String) result[3],  // metodoPago
                        (String) result[4],  // agenteCompra
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
        // Obtener los resultados de la consulta nativa
        //Por el momento yo mismo le asigno el id que usará para buscar las órdenes asignadas
        //pues este id será proporcionado recién automáticamente cuando se realice el LOGIN
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeEnProceso(4);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (String) result[3],  // metodoPago
                        (String) result[4],  // agenteCompra
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
        // Obtener los resultados de la consulta nativa
        //Por el momento yo mismo le asigno el id que usará para buscar las órdenes asignadas
        //pues este id será proporcionado recién automáticamente cuando se realice el LOGIN
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeResuelto(4);

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (String) result[2],  // fechaCreacion
                        (String) result[3],  // metodoPago
                        (String) result[4],  // agenteCompra
                        (Long) result[5],  //idAgenteCompra
                        (String) result[6], //idEstadoPedido
                        (Integer) result[7] //idOrden
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/solveOrdersTable-Agente";
    }
    @GetMapping("/perfil")
    public String showPerfil() {

        return "Agente/profile-Agente";
    }

    //Información de órdenes de usuarios asignados
    //OJO: Se crean múltiples vistas de estos detalles con el objetivo de mostrar el html
    //mas una vez se defina en el backend la lógica solo será necesario una vista que
    //varíe según corresponda el estado

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


    //Información de órdenes de usuarios asignados (este es el definitivo xD)
    @GetMapping("/detailsOrder")
    public String showDetails(Model model, @RequestParam ("idOrden") int idOrden,
                                           @RequestParam ("idUsuario") int idUsuario){

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


    //Tableros de USUARIOS

    @GetMapping("/allUsers")
    public String showAllUsers(Model model) {
        //Por el momento yo mismo le asigno el id que usará para buscar los usuarios asignadas
        //pues este id será proporcionado recién automáticamente cuando se realice el LOGIN
        List<Usuario> usuarioList = usuarioRepository.findByAgentcompraIdusuario_Id(4);
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
                        (String) result[3],  // metodoPago
                        (String) result[4],  // agenteCompra
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
            @RequestParam("idImportador") int idImportador,
            @RequestParam("banReason")
            @Size(max = 150, message = "La razón del baneo no puede exceder los 150 caracteres")
            @NotBlank(message = "La razón del baneo es obligatoria") String banReason,
            RedirectAttributes redirectAttributes) {

        // Buscamos al importador por su ID
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idImportador);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Cambiar el estado del usuario a baneado (isActivated = 0)
            usuario.setIsActivated((byte) 0);
            // Establecer el motivo del baneo
            usuario.setMotivoBaneo(banReason);
            // Guardar los cambios en la base de datos
            usuarioRepository.save(usuario);

            // Agregar mensaje de éxito al flash attributes
            redirectAttributes.addFlashAttribute("mensaje", "El usuario ha sido baneado con éxito.");
        } else {
            // Si no se encuentra el usuario
            redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado.");
        }

        // Redirigir a la página de usuarios baneados
        return "redirect:/agente/baneadosUsers";
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
