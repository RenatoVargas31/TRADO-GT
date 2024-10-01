package com.app.tradogt.controller;

import com.app.tradogt.dto.OrdenCompraAgtDto;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.repository.OrdenRepository;
import com.app.tradogt.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agente")
public class AgenteComprasController {
    final UsuarioRepository usuarioRepository;
    final OrdenRepository ordenRepository;

    public AgenteComprasController(UsuarioRepository usuarioRepository, OrdenRepository ordenRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ordenRepository = ordenRepository;
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
        // Obtener los resultados de la consulta nativa
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNative();

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (String) result[1],  // fechaCreacion
                        (String) result[2],  // metodoPago
                        (String) result[3],  // agenteCompra
                        (String) result[4]   // estadoPedido
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/allOrdersTable-Agente";
    }
    @GetMapping("/sinAsignarOrders")
    public String showSinAsignarOrders(Model model) {
        // Obtener los resultados de la consulta nativa
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeSinAsignar();

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (String) result[1],  // fechaCreacion
                        (String) result[2],  // metodoPago
                        (String) result[3],  // agenteCompra
                        (String) result[4]   // estadoPedido
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);

        return "Agente/sinAsignarOrdersTable-Agente";
    }
    @GetMapping("/pendingOrders")
    public String showPendingOrders(Model model) {
        // Obtener los resultados de la consulta nativa
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativePendiente();

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (String) result[1],  // fechaCreacion
                        (String) result[2],  // metodoPago
                        (String) result[3],  // agenteCompra
                        (String) result[4]   // estadoPedido
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/pendingOrdersTable-Agente";
    }
    @GetMapping("/enProcesoOrders")
    public String showEnProcesoOrders(Model model) {
        // Obtener los resultados de la consulta nativa
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeEnProceso();

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (String) result[1],  // fechaCreacion
                        (String) result[2],  // metodoPago
                        (String) result[3],  // agenteCompra
                        (String) result[4]   // estadoPedido
                ))
                .collect(Collectors.toList());
        model.addAttribute("listaOrdenes", listaOrdenes);
        return "Agente/procesoOrdersTable-Agente";
    }
    @GetMapping("/solveOrders")
    public String showSolveOrders(Model model) {
        // Obtener los resultados de la consulta nativa
        List<Object[]> resultados = ordenRepository.getOrderDetailsAsDtoNativeResuelto();

        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraAgtDto> listaOrdenes = resultados.stream()
                .map(result -> new OrdenCompraAgtDto(
                        (String) result[0],  // usuarioPropietario
                        (String) result[1],  // fechaCreacion
                        (String) result[2],  // metodoPago
                        (String) result[3],  // agenteCompra
                        (String) result[4]   // estadoPedido
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


    //Tableros de USUARIOS
    @GetMapping("/allUsers")
    public String showAllUsers(Model model) {
        List<Usuario> usuarioList = usuarioRepository.findAll();
        model.addAttribute("usuarioList", usuarioList);

        return "Agente/allUsersTable-Agente";
    }


    @GetMapping("/habilitadosUsers")
    public String showHabilitadosUsers(Model model) {
        model.addAttribute("usuarioList", usuarioRepository.findByBanned(1));
        return "Agente/habilitadosUsersTable-Agente";
    }
    @GetMapping("/baneadosUsers")
    public String showBaneadosUsers(Model model) {
        model.addAttribute("usuarioList", usuarioRepository.findByBanned(0));
        return "Agente/baneadosUsersTable-Agente";
    }

    //Información sobre usuarios asignados a agentes
    @GetMapping("/infoUsuario")
    public String showInfoUser(){
        return "Agente/detallesUsuarios-Agente";
    }
    @GetMapping("/infoUsuarioBaneado")
    public String showInfoUserBan(){
        return "Agente/detallesUsuariosBaneados-Agente";
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
