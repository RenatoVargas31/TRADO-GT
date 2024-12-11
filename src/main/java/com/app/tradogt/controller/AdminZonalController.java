package com.app.tradogt.controller;

import com.app.tradogt.dto.*;
import com.app.tradogt.entity.*;
import com.app.tradogt.helpers.PasswordGenerator;
import com.app.tradogt.services.NotificationCorreoService;
import com.app.tradogt.services.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.app.tradogt.repository.*;
import org.springframework.ui.Model;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/adminzonal")
public class AdminZonalController {

    //Reposirotios
    final OrdenRepository ordenRepository;
    final UsuarioRepository usuarioRepository;
    final ProductosRepository productosRepository;
    final ProductoEnZonaRepository productoEnZonaRepository;
    private final DistritoRepository distritoRepository;
    private final RolRepository rolRepository;
    private final ZonaRepository zonaRepository;

    public AdminZonalController(OrdenRepository ordenRepository, UsuarioRepository usuarioRepository, ProductosRepository productosRepository, ProductoEnZonaRepository productoEnZonaRepository, DistritoRepository distritoRepository, RolRepository rolRepository, ZonaRepository zonaRepository) {
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
        this.productosRepository = productosRepository;
        this.productoEnZonaRepository = productoEnZonaRepository;
        this.distritoRepository = distritoRepository;
        this.rolRepository = rolRepository;
        this.zonaRepository = zonaRepository;
    }

    @Autowired
    private NotificationCorreoService notificationCorreoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;

    private int getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;

        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername();
        } else {
            correoUsuario = principal.toString();
        }

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
        return usuario.getId();
    }
    private Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String correoUsuario;

        if (principal instanceof UserDetails) {
            correoUsuario = ((UserDetails) principal).getUsername();
        } else {
            correoUsuario = principal.toString();
        }

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
        return usuario;
    }

    @GetMapping("/vacio")
    public String home() {
        return "AdminZonal/starter-AdminZonal";
    }

    @GetMapping("/notificaciones")
    @ResponseBody
    public List<Notificacion> obtenerNotificaciones(@RequestParam("usuarioId") int usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).get();
        return notificationService.getUnreadNotifications(usuario);
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        int zonaId = getAuthenticatedUser().getZonaIdzona().getId();
        model.addAttribute("active", usuarioRepository.countUsuariosActivos(zonaId));
        model.addAttribute("inactive", usuarioRepository.countUsuariosInactivos(zonaId));
        model.addAttribute("usuariosTotal", usuarioRepository.countUsuarios(zonaId));
        model.addAttribute("stockTotal", productoEnZonaRepository.countStockTotal(zonaId));
        model.addAttribute("stockPromedio", productoEnZonaRepository.stockPorProducto(zonaId));
        model.addAttribute("productoStockMenor", productoEnZonaRepository.productStockMenor(zonaId).get(0));
        model.addAttribute("agentes", usuarioRepository.getAgentesbyZonal(zonaId));
        return "AdminZonal/dashboard-AdminZonal";
    }

    @GetMapping("/fechasArribo")
    public String showFechasArribo(Model model) {
        int idZonaAdmin = getAuthenticatedUser().getZonaIdzona().getId();
        List<Object[]> resultados = ordenRepository.getOrdersByZonaAdminZonal(idZonaAdmin);
        // Mapear los resultados al DTO directamente en el controlador
        List<OrdenCompraZonalDto> ordenesConFechas = resultados.stream()
                .map(result -> new OrdenCompraZonalDto(
                        (String) result[0],  // usuarioPropietario
                        (Integer) result[1],  // idUsuario
                        (Date) result[2],  // fechaCreacion
                        (Date) result[3],  // fechaRecibido
                        (BigDecimal) result[4],  // montoTotal
                        (String) result[5],  // codigoOrden
                        (Long) result[6],  //idAgenteCompra
                        (String) result[7], //idEstadoPedido
                        (Integer) result[8], //idOrden
                        (String) result[9] //Nombre distrito
                ))
                .collect(Collectors.toList());
        model.addAttribute("ordenesConFechas", ordenesConFechas);
        return "AdminZonal/tablaFechaArribo-AdminZonal";
    }

    @GetMapping("/reposicionProductos")
    public String showReposicionProductos(Model model) {
        int idAdminZonal = getAuthenticatedUserId();
        List<ProductoEnZona> productos = productoEnZonaRepository.findAllByZonaIdzonaAndIsDeleted(zonaRepository.findById(1).get(), Byte.parseByte("0"));
        model.addAttribute("productos", productos);
        return "AdminZonal/tablaReposicionProductos-AdminZonal";
    }

    //Editar el estado de reposición
    @PostMapping("/editarEstadoRepo")
    public String editarEstadoRepo(@RequestParam("productoId") int productoId,
                                   @RequestParam("zonaId") int zonaId,
                                   RedirectAttributes redirectAttributes) {

        // Buscar ProductoEnZona usando productoId y zonaId
        Optional<ProductoEnZona> productoEnZona = productoEnZonaRepository.findByIdAndZona(productoId, zonaId);

        if (productoEnZona.isPresent()) {
            ProductoEnZona producto = productoEnZona.get();

            // Cambiar el estado dependiendo de la cantidad de stock
            if (producto.getCantidad() < 25) {
                producto.setEstadoRepo((byte) 2);  // Cambia a 'En espera de reposición'
            } else {
                producto.setEstadoRepo((byte) 0);  // Cambia a 'Stock adecuado'
            }

            // Guardar los cambios
            productoEnZonaRepository.save(producto);
            redirectAttributes.addFlashAttribute("mensaje", "El estado del producto ha sido actualizado.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
        }

        return "redirect:/adminzonal/reposicionProductos";
    }



    @GetMapping("/faq")
    public String showFaq() {
        return "AdminZonal/faq";
    }

    @PostMapping("/subirFoto")
    public String viewSubirFoto(@RequestParam("foto") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Por favor, selecciona una foto");
            return "redirect:/adminzonal/perfil";
        }

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
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("message", "Usuario no autenticado.");
                return "redirect:/adminzonal/perfil";
            }

            // Actualizar el nombre de la foto en la base de datos
            usuario.setFoto(file.getOriginalFilename());
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("message", "Carga satisfactoria '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Error al cargar '" + file.getOriginalFilename() + "'");
        }

        // Redirigir al perfil
        return "redirect:/adminzonal/perfil";
    }




    @GetMapping("/perfil")
    public String showPerfil (@ModelAttribute("user") Usuario user,  Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(getAuthenticatedUserId());
        if (usuario.isPresent()) {
            model.addAttribute("user", usuario.get());
            return "AdminZonal/profile";
        } else {
            return "redirect:/logout";
        }

    }

    @PostMapping("/editarPerfil")
    public String editarPerfil(@ModelAttribute("user") Usuario user, Model model) {
        Usuario usuario = usuarioRepository.findById(getAuthenticatedUserId()).get();
        usuario.setDireccion(user.getDireccion());
        usuario.setTelefono(user.getTelefono());
        usuarioRepository.save(usuario);
        return "redirect:/adminzonal/perfil";
    }

    @GetMapping("/contraseña")
    public String showPassword(Model model) {
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "AdminZonal/password";
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
            return "AdminZonal/password";  // Retorna a la vista con los errores
        }

        // Obtener el usuario autenticado desde el sistema de seguridad
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());

        // Verificar si la contraseña actual ingresada coincide con la almacenada
        if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), usuario.getContrasena())) {
            model.addAttribute("error", "La contraseña actual es incorrecta.");
            return "AdminZonal/password";  // Retorna a la vista con el mensaje de error
        }

        // Verificar si las contraseñas nuevas coinciden
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "AdminZonal/password";  // Retorna a la vista con el mensaje de error
        }

        // Actualizar la contraseña del usuario
        usuario.setContrasena(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        usuarioRepository.save(usuario);  // Guardar los cambios en la base de datos

        // Agregar mensaje de éxito a los flash attributes
        redirectAttributes.addFlashAttribute("exito", "Contraseña cambiada con éxito.");

        notificationCorreoService.enviarCorreoCambioContraseña(usuario.getCorreo(),usuario.getNombre());

        return "redirect:/adminzonal/perfil";  // Redirige a la página del perfil
    }

    @GetMapping("/gestionAgente")
    public String showGestionAgente(Model model) {
        /*List<Object[]> usuarioDetails = usuarioRepository.getUsuarioOrderProductDetails();
        model.addAttribute("usuarioDetails", usuarioDetails);*/

        /*List<Usuario> agentes = usuarioRepository.findAllByAdmzonalIdusuario_IdAndIsActivated(2, Byte.parseByte("1"));
        model.addAttribute("agentes", agentes);*/

        List<AgenteInfoZon> agentes = usuarioRepository.getAgentesbyZonal(getAuthenticatedUserId());
        model.addAttribute("agentes", agentes);
        model.addAttribute("agenteSize",agentes.size());

        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);

        //Obtenemos usuario
        int userid =getAuthenticatedUser().getId();

        List<Object[]> ordenes = ordenRepository.findAllByAgentcompraIdusuario(userid);
        model.addAttribute("ordenes", ordenes);


        return "AdminZonal/gestionAgente-AdminZonal"; }

    @GetMapping("/nuevoAgente")
    public String showNuevoAgente(Model model, @ModelAttribute("agente") Usuario usuario) {

        int zonaIdZonal = getAuthenticatedUser().getZonaIdzona().getId();
        model.addAttribute("listaDistritos", distritoRepository.findByZonaIdzonaId(zonaIdZonal));
        Optional<Usuario> AgentePrueba = usuarioRepository.findById(zonaIdZonal);
        List<AgenteInfoZon> agentes = usuarioRepository.getAgentesbyZonal(getAuthenticatedUserId());
        model.addAttribute("agenteSize", agentes.size());
        model.addAttribute("agenteA", AgentePrueba.get());
        return "AdminZonal/nuevoAgente-AdminZonal";
    }

    @PostMapping("/saveAgente")
    public String saveAgente(@ModelAttribute("agente") Usuario usuario, BindingResult bindingResult, RedirectAttributes attr, Model model) {
        int zonaIdZonal = getAuthenticatedUser().getZonaIdzona().getId();
        //Vereficamos si no hay mas de 3 agentes asignados/ creados
        int agentes = usuarioRepository.countAgenteACargo(getAuthenticatedUserId());


        // Verificar si el correo ya está registrado en la base de datos
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            bindingResult.rejectValue("correo", "error.correo", "El correo electrónico ya está registrado.");
        }
        // Verificar si el DNI ya está registrado en la base de datos
        if (usuarioRepository.existsByDni(usuario.getDni())) {
            bindingResult.rejectValue("dni", "error.dni", "El DNI ya está registrado.");
        }
        // Verificar si hay errores de validación (incluido el correo ya existente)
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaDistritos", distritoRepository.findByZonaIdzonaId(zonaIdZonal));
            return "AdminZonal/nuevoAgente-AdminZonal"; // Vuelve al formulario con el mensaje de error
        } else if (agentes>=3){
            attr.addFlashAttribute("ErrorAgente", "Has alcanzado el límite de 3 agentes de compra a cargo.");
            return "AdminZonal/nuevoAgente-AdminZonal";
        }else {
            //Asignar una contraseña por random de 10 dígitos y que combine número y letras
            String password = PasswordGenerator.generateRandomPassword();
            System.out.println(password);
            usuario.setContrasena(passwordEncoder.encode(password));




            // Si no hay errores, proceder con el guardado del usuario
            attr.addFlashAttribute("msg", "Agente " + (usuario.getId() == null ? "creado exitosamente" : "actualizado exitosamente"));
            usuario.setIsActivated(Byte.parseByte("1"));
            usuario.setIsAccepted(Byte.parseByte("1"));
            usuario.setAdmzonalIdusuario(getAuthenticatedUser());
            usuario.setRolIdrol(rolRepository.findById(3).get());
            usuario.setZonaIdzona(zonaRepository.findById(zonaIdZonal).get());
            usuarioRepository.save(usuario);

            String enlaceFeik = "holi";

            notificationCorreoService.enviarCorreoCreacionCuentaAgente(usuario.getCorreo(),usuario.getNombre(),password,enlaceFeik);
            return "redirect:/adminzonal/gestionAgente"; // Redirigir a la lista de agentes
        }
    }



    @GetMapping("/verAgente/{id}")
    public String showVerAgente(@PathVariable("id") Integer usuarioId, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        model.addAttribute(usuario.get());
        return "AdminZonal/verAgente-AdminZonal";
    }

    @PostMapping("/editarFecha")
    public String editarFechaArribo(
            @RequestParam("orderId") String orderId,
            @RequestParam("fechaArribo") LocalDate fechaArribo,
            RedirectAttributes redirectAttributes) {

        // Validación manual: Verificar si el ID de la orden es nulo o vacío
        if (orderId == null || orderId.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El ID de la orden es obligatorio.");
            return "redirect:/adminzonal/fechasArribo";
        }

        // Validación manual: Verificar si la fecha de arribo es nula
        if (fechaArribo == null) {
            redirectAttributes.addFlashAttribute("error", "La fecha de arribo es obligatoria.");
            return "redirect:/adminzonal/fechasArribo";
        }

        // Validación personalizada: La fecha de arribo no puede ser anterior al día actual
        if (fechaArribo.isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "La fecha de arribo no puede ser anterior al día actual.");
            return "redirect:/adminzonal/fechasArribo";
        }

        // Buscar la orden por su ID
        Optional<Orden> optionalOrden = ordenRepository.findByCodigo(orderId);

        if (optionalOrden.isPresent()) {
            Orden orden = optionalOrden.get();
            // Actualizar la fecha de arribo
            orden.setFechaRecibido(fechaArribo);
            ordenRepository.save(orden);

            // Añadir mensaje de éxito
            redirectAttributes.addFlashAttribute("mensaje", "La fecha de arribo se ha actualizado correctamente.");
        } else {
            // Si no se encuentra la orden
            redirectAttributes.addFlashAttribute("error", "No se pudo encontrar la orden con el ID proporcionado.");
        }

        // Redirigir a la página de fechas de arribo
        return "redirect:/adminzonal/fechasArribo";
    }

    @GetMapping("/api/productos")
    @ResponseBody
    public List<Map<String, Object>> getProductosPorZona() {
        int zonaId = getAuthenticatedUser().getZonaIdzona().getId();
        List<ProductoEnZona> productos = productoEnZonaRepository.findStockBajoZona(zonaId);

        // Convertir la lista de productos en una lista de mapas
        return productos.stream()
                .map(productoEnZona -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", productoEnZona.getProductoIdproducto().getNombre()); // Asegúrate de que este método exista
                    map.put("stock", productoEnZona.getCantidad()); // Asegúrate de que este método exista
                    return map;
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/api/top10")
    @ResponseBody
    public List<Map<String, Object>> getTop10() {
        int zonaId = getAuthenticatedUser().getZonaIdzona().getId();
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductoEnZona> productos = productoEnZonaRepository.findTop10Products(zonaId, pageable);

        // Convertir la lista de productos en una lista de mapas
        return productos.stream()
                .map(productoEnZona -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", productoEnZona.getProductoIdproducto().getNombre()); // Asegúrate de que este método exista
                    map.put("stock", productoEnZona.getCantidad()); // Asegúrate de que este método exista
                    return map;
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/api/top10Importadores")
    @ResponseBody
    public List<Map<String, Object>> getTop10Importadores() {
        // Obtener el ID de la zona del usuario autenticado
        int zonaId = getAuthenticatedUser().getZonaIdzona().getId();

        // Crear objeto Pageable para la paginación
        Pageable pageable = PageRequest.of(0, 10);

        // Recuperar la lista de importadores usando el repositorio
        List<ImportacionesporImportador> importadores = productoEnZonaRepository.findTop10Importadores(zonaId, pageable);

        // Convertir la lista de importadores en una lista de mapas
        return importadores.stream()
                .map(importador -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", importador.getTienda()); // Asegúrate de que este método exista
                    map.put("stock", importador.getSumaCantidades()); // Asegúrate de que este método exista
                    return map;
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/api/countUsuarios")
    @ResponseBody
    public Map<String, Object> getCountUsuarios() {
        // Obtener el porcentaje de usuarios activos
        double porcentajeActivos = usuarioRepository.porcentajeUsuariosActivos(getAuthenticatedUser().getZonaIdzona().getId());

        System.out.println("Holaaaaa "+ porcentajeActivos);
        // Crear el mapa de respuesta con los porcentajes
        Map<String, Object> response = new HashMap<>();
        response.put("active", porcentajeActivos); // Porcentaje de usuarios activos
        response.put("inactive", 100 - porcentajeActivos); // Porcentaje de usuarios inactivos

        return response;
    }







}
