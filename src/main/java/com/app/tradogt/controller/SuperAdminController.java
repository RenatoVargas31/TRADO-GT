package com.app.tradogt.controller;

import com.app.tradogt.dto.PasswordChangeDto;
import com.app.tradogt.dto.ProveedorValDto;
import com.app.tradogt.entity.*;
import com.app.tradogt.helpers.PasswordGenerator;
import com.app.tradogt.helpers.ProductCodeGenerator;
import com.app.tradogt.repository.*;
import com.app.tradogt.services.NotificationCorreoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/superadmin")
@SessionAttributes("superAdminAuth")
public class SuperAdminController {

    //<editor-fold desc="Repositories">
    final ProveedorRepository proveedorRepository;
    final UsuarioRepository usuarioRepository;
    final ZonaRepository zonaRepository;
    final RolRepository rolRepository;
    final DistritoRepository distritoRepository;
    final ProductosRepository productosRepository;
    final ProductoEnZonaRepository productoEnZonaRepository;
    final SubCategoriaRepository subCategoriaRepository;
    final CategoriaRepository categoriaRepository;

    @Autowired
    private NotificationCorreoService notificationCorreoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public SuperAdminController(DataSource dataSource, ProveedorRepository proveedorRepository, UsuarioRepository usuarioRepository, ZonaRepository zonaRepository, RolRepository rolRepository, DistritoRepository distritoRepository, ProductosRepository productosRepository, ProductoEnZonaRepository productoEnZonaRepository, SubCategoriaRepository subCategoriaRepository, CategoriaRepository categoriaRepository) {
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.zonaRepository = zonaRepository;
        this.rolRepository = rolRepository;
        this.distritoRepository = distritoRepository;
        this.productosRepository = productosRepository;
        this.productoEnZonaRepository = productoEnZonaRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.categoriaRepository = categoriaRepository;
    }
    //</editor-fold>
    @GetMapping("/perfil")
    public String viewPerfil(Model model) {
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "SuperAdmin/perfil-SAdmin";
    }
    @GetMapping("/editarPerfilForm")
    public String viewEditarPerfil(Model model) {

        return "SuperAdmin/perfilEdit-SAdmin";
    }
    @PostMapping("/editarPerfil")
    public String viewEditarPerfil(Usuario usuario, Model model) {
        // Actualizar el usuario en la base de datos
        Usuario usuarioActual = (Usuario) model.getAttribute("usuarioAutenticado");
        assert usuarioActual != null;
        usuarioActual.setTelefono(usuario.getTelefono());
        usuarioActual.setDireccion(usuario.getDireccion());
        //Guardar
        usuarioRepository.save(usuarioActual);
        return "redirect:/superadmin/perfil";
    }

    @PostMapping("/subirFoto")
    public String viewSubirFoto(@RequestParam("foto") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Por favor, seleecciona una foto");
            return "redirect:/superadmin/perfil";
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

            // Obtener el usuario autenticado
            Usuario usuario = (Usuario) model.getAttribute("usuarioAutenticado");
            assert usuario != null;

            // Actualizar el nombre de la foto en la base de datos
            usuario.setFoto(file.getOriginalFilename());
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("message", "Carga satisfactoria '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Error al cargar '" + file.getOriginalFilename() + "'");
        }

        // Redirigir al perfil
        return "redirect:/superadmin/perfil";
    }
    @GetMapping("/changePassForm")
    public String viewChangePassForm(Model model) {
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "SuperAdmin/changePass-SAdmin";
    }

    @PostMapping("/changePass")
    public String viewChangePass(@Valid PasswordChangeDto passwordChangeDto,
                                 BindingResult result,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        // Validación de errores
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "SuperAdmin/changePass-SAdmin";  // Retorna a la vista con los errores
        }

        // Obtener el usuario autenticado desde el sistema de seguridad
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());

        // Verificar si la contraseña actual ingresada coincide con la almacenada
        if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), usuario.getContrasena())) {
            model.addAttribute("error", "La contraseña actual es incorrecta.");
            return "SuperAdmin/changePass-SAdmin";  // Retorna a la vista con el mensaje de error
        }

        // Verificar si las contraseñas nuevas coinciden
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
            return "SuperAdmin/changePass-SAdmin";  // Retorna a la vista con el mensaje de error
        }

        // Actualizar la contraseña del usuario
        usuario.setContrasena(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        usuarioRepository.save(usuario);  // Guardar los cambios en la base de datos

        // Agregar mensaje de éxito a los flash attributes
        redirectAttributes.addFlashAttribute("exito", "Contraseña cambiada con éxito.");

        notificationCorreoService.enviarCorreoCambioContraseña(usuario.getCorreo(), usuario.getNombre());

        return "redirect:/superadmin/perfil";  // Redirige a la página del perfil
    }
    //<editor-fold desc="CRUD Menu">
    @GetMapping("/inicio")
    public String viewInicio(Model model) {
        //Listar usuarios que tenga id rol 2 (Administrador Zonal) y isAccepted 1
        List<Usuario> admZonales = usuarioRepository.findAllByRolIdrolIdAndIsAccepted(2, (byte) 1);
        //Listar usuarios que tenga id rol 3 (Agente de Compra) y isAccepted 1
        List<Usuario> agentes = usuarioRepository.findAllByRolIdrolIdAndIsAccepted(3, (byte) 1);
        //Listar usuarios que tenga id rol 4 (Importador) y isAccepted 1
        List<Usuario> importadores = usuarioRepository.findAllByRolIdrolIdAndIsAccepted(4, (byte) 1);
        //Enviar a la vista las listas
        model.addAttribute("administradores", admZonales);
        model.addAttribute("agentes", agentes);
        model.addAttribute("importadores", importadores);
        return "SuperAdmin/inicio-SAdmin";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        List<Usuario> AgenteCompra = usuarioRepository.getUsuariosByRol(3);
        List<Usuario> UsuariosBaneados = usuarioRepository.getUsuariosInactivos();
        List<Proveedor> ProveedoresBaneados = proveedorRepository.getProveedorBaneado();
        List<ProveedorValDto> proveedorMayorVal = proveedorRepository.getProveedorMayorValoracion();
        List<ProveedorValDto> proveedorMenorVal = proveedorRepository.getProveedorMenorValoracion();
        //Enviar a la vista
        model.addAttribute("AgenteCompra", AgenteCompra);
        model.addAttribute("UsuariosBaneados", UsuariosBaneados);
        model.addAttribute("ProveedoresBaneados", ProveedoresBaneados);
        model.addAttribute("proveedorMayorVal", proveedorMayorVal);
        model.addAttribute("proveedorMenorVal", proveedorMenorVal);
        return "SuperAdmin/dashboard-SAdmin";
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Administrador Zonal (Completo - terminado)">
    //<editor-fold desc="Nuevo Administrador Zonal">
    @GetMapping("/admZonalNuevoForm")
    public String viewAdmZonalNuevoForm(Model model) {
        //Enviar un usuario
        model.addAttribute("usuario", new Usuario());
        //Enviar lista de zonas
        model.addAttribute("zonas", zonaRepository.findAll());
        //Enviar rol de id 2 (Administrador Zonal)
        model.addAttribute("rol", rolRepository.findByNombre("Administrador Zonal"));
        return "SuperAdmin/admZonalNuevo-SAdmin";
    }
    @PostMapping("/admZonalNuevo")
    public String viewAdmZonalNuevoForm(@ModelAttribute Usuario usuario) {
        //Asignar una contraseña por random de 10 dígitos y que combine número y letras
        String password = PasswordGenerator.generateRandomPassword();
        System.out.println(password);
        //Encriptar la contraseña con BCrypt de 10 rondas
        String passwordEncrypted = BCrypt.hashpw(password, BCrypt.gensalt(10));
        usuario.setContrasena(passwordEncrypted);
        //Guardar usuario
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/admZonalNuevoForm";
    }
    //</editor-fold>

    //<editor-fold desc="Editar Administrador Zonal">
    @PostMapping("/admZonalEditarForm")
    public String viewAdmZonalEditarForm(Model model, Integer id) {
        // Buscar proveedor por id y obtener el usuario directamente
        Usuario usuario = usuarioRepository.findById(id).get();
        // Enviar a la vista
        model.addAttribute("usuario", usuario);
        // Enviar lista de zonas
        model.addAttribute("zonas", zonaRepository.findAll());
        // Enviar rol de id 2 (Administrador Zonal)
        model.addAttribute("rol", rolRepository.findByNombre("Administrador Zonal"));
        return "SuperAdmin/admZonalEditar-SAdmin";
    }
    @PostMapping("/admZonalEditar")
    public String viewAdmZonalEditar(Usuario usuario) {
        //Actualizar usuario existente con Optional
        Usuario usuarioActual = usuarioRepository.findById(usuario.getId()).get();
        usuarioActual.setCorreo(usuario.getCorreo());
        usuarioActual.setTelefono(usuario.getTelefono());
        usuarioActual.setZonaIdzona(usuario.getZonaIdzona());
        usuarioRepository.save(usuarioActual);

        if (usuario.getIsActivated() == 1) {
            return "redirect:/superadmin/admZonalActivos";
        } else {
            return "redirect:/superadmin/admZonalInactivos";
        }
    }
    //</editor-fold>

    //<editor-fold desc="Listas Administrador Zonal">
    @GetMapping("/admZonalActivos")
    //Listar usuarios que tengan id rol 2 (Administrador Zonal) y isActivo 1
    public String viewAdmZonalActivos(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAllByRolIdrolIdAndIsActivated(2, (byte) 1);
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/admZonalActivos-SAdmin";
    }

    @GetMapping("/admZonalInactivos")
    public String viewAdmZonalInactivos(Model model) {
        //Listar usuarios que tengan id rol 2 (Administrador Zonal) y isActivo 0
        List<Usuario> usuarios = usuarioRepository.findAllByRolIdrolIdAndIsActivated(2, (byte) 0);
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/admZonalInactivos-SAdmin";
    }
    //</editor-fold>
    //<editor-fold desc="Baneo y desbaneo de AdministradorZonal">
    @GetMapping("/admZonalBorrar")
    public String viewAdmZonalBorrar(Integer id) {
        // Retrieve the user by id
        Usuario usuario = usuarioRepository.findById(id).get();
        // Set isActivated to 0 for logical deletion
        usuario.setIsActivated((byte) 0);
        // Save the updated user
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/admZonalActivos";
    }
    @GetMapping("/admZonalReactivar")
    public String viewAdmZonalReactivar(Integer id) {
        // Retrieve the user by id
        Usuario usuario = usuarioRepository.findById(id).get();
        // Set isActivated to 0 for logical deletion
        usuario.setIsActivated((byte) 1);
        // Save the updated user
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/admZonalInactivos";
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="CRUD Agente de Compra (RUD - terminado)">
    @PostMapping("/agentCompraEditarForm")
    public String viewAgentCompraEditarForm(Model model, Integer id) {
        //Buscar proveedor por id
        Usuario usuario = usuarioRepository.findById(id).get();
        //Enviar a la vista
        model.addAttribute("usuario", usuario);
        //Enviar lista de distritos
        model.addAttribute("distritos", distritoRepository.findByZonaIdzonaId(usuario.getDistritoIddistrito().getZonaIdzona().getId()));
        //Enviar rol de id 3 (Agente de Compra)
        model.addAttribute("rol", rolRepository.findById(3).orElseThrow());
        return "SuperAdmin/agentCompraEditar-SAdmin";
    }

    @PostMapping("/agentCompraEditar")
    public String viewAgentCompraEditar(Usuario usuario) {
        //Actualizar usuario existente sin query method
        Usuario usuarioActual = usuarioRepository.findById(usuario.getId()).get();
        usuarioActual.setCorreo(usuario.getCorreo());
        usuarioActual.setTelefono(usuario.getTelefono());
        usuarioActual.setDireccion(usuario.getDireccion());
        usuarioActual.setDistritoIddistrito(usuario.getDistritoIddistrito());
        usuarioActual.setRazonSocial(usuario.getRazonSocial());
        usuarioActual.setCodigoDespachador(usuario.getCodigoDespachador());
        usuarioRepository.save(usuarioActual);

        if (usuario.getIsActivated() == 1) {
            return "redirect:/superadmin/agentCompraActivos";
        } else {
            return "redirect:/superadmin/agentCompraInactivos";
        }
    }

    @GetMapping("/agentCompraPostula")
    public String viewAgentCompraPostula(Model model) {
        List<Usuario> solicitudes = usuarioRepository.findAllByRolIdrolIdAndIsPostulated(4, (byte) 1);
        //Enviar a la vista
        model.addAttribute("solicitudes", solicitudes);
        return "SuperAdmin/agentCompraPostula-SAdmin";
    }

    @GetMapping("/agentCompraActivos")
    public String viewAgentCompraActivos(Model model) {
        //Listar usuarios con rol 3, isActivo 1, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolIdrolIdAndIsActivated(3, (byte) 1);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/agentCompraActivos-SAdmin";
    }

    @GetMapping("/agentCompraInactivos")
    public String viewAgentCompraInactivos(Model model) {
        //Listar usuarios con rol 3, isActivo 0, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolIdrolIdAndIsActivated(3, (byte) 0);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/agentCompraInactivos-SAdmin";
    }

    @GetMapping("/agentCompraBorrar")
    public String viewAgentCompraBorrar(Integer id) {
        //Borrado lógico del proveedor sin query method
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setIsActivated((byte) 0);
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/agentCompraActivos";
    }
    @GetMapping("/agentCompraReactivar")
    public String viewAgentCompraReactivar(Integer id) {
        //Reactivar usuario sin query method
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setIsActivated((byte) 1);
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/agentCompraInactivos";
    }

    @GetMapping("/agentCompraPostulanteApto")
    public String viewAgentCompraPostulanteApto(Integer id, RedirectAttributes redirectAttributes) {
        // Actualizar usuario a Agente de Compra
        Usuario usuario = usuarioRepository.findById(id).get();

        // Asignar un administrador zonal aleatorio de la misma zona del usuario
        List<Usuario> admZonales = usuarioRepository.findAllByRolIdrolIdAndIsActivatedAndZonaIdzonaId(2, (byte) 1, usuario.getDistritoIddistrito().getZonaIdzona().getId());
        boolean admZonalAsignado = false;
        for (Usuario admZonal : admZonales) {
            if (usuarioRepository.findAllByAdmzonalIdusuario_IdAndIsActivated(admZonal.getId(),(byte) 1).size() < 4) {
                usuario.setAdmzonalIdusuario(admZonal);
                usuario.setRolIdrol(rolRepository.findById(3).get());
                usuario.setIsPostulated((byte) 0);
                usuarioRepository.save(usuario);
                admZonalAsignado = true;
                break;
            }
        }

        if (admZonalAsignado) {
            redirectAttributes.addFlashAttribute("successMessage", "Se ascendió a Agente de Compra correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "La cantidad de Agentes de Compra está completa en la zona del postulante.");
        }

        return "redirect:/superadmin/agentCompraPostula";
    }

    @GetMapping("/agentCompraPostulanteNoApto")
    public String viewAgentCompraPostulanteNoApto(Integer id) {
        //Borrar la postulación
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setIsPostulated((byte) 0);
        usuario.setCodigoDespachador(null);
        usuario.setRazonSocial(null);
        usuario.setRuc(null);
        usuario.setAdmzonalIdusuario(null);
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/agentCompraPostula";
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Importador (RUD - terminado)">
    @PostMapping("/importadorEditarForm")
    public String viewImportadorEditarForm(Model model, Integer id) {
        //Buscar proveedor por id
        Usuario usuario = usuarioRepository.findById(id).get();
        //Enviar a la vista
        model.addAttribute("usuario", usuario);
        //Enviar lista de distritos
        model.addAttribute("distritos", distritoRepository.findAll());
        //Enviar rol de id 4 (Importador)
        model.addAttribute("rol", rolRepository.findById(4).orElseThrow());
        return "SuperAdmin/importadorEditar-SAdmin";
    }

    @PostMapping("/importadorEditar")
    public String viewImportadorEditar(Usuario usuario) {
        //Actualizar usuario existente sin query method
        Usuario usuarioActual = usuarioRepository.findById(usuario.getId()).get();
        usuarioActual.setCorreo(usuario.getCorreo());
        usuarioActual.setDireccion(usuario.getDireccion());
        usuarioActual.setDistritoIddistrito(usuario.getDistritoIddistrito());
        usuarioRepository.save(usuarioActual);
        if (usuario.getIsActivated() == 1) {
            return "redirect:/superadmin/importadorActivos";
        } else {
            return "redirect:/superadmin/importadorInactivos";
        }
    }

    @GetMapping("/importadorSolicitud")
    public String viewImportadorSolicitud(Model model) {
        //Listar usuarios con rol 4, isActivo 0, isAceptado 0
        List<Usuario> solicitudes = usuarioRepository.findAllByIsAccepted((byte) 0);
        //Enviar a la vista
        model.addAttribute("solicitudes", solicitudes);
        return "SuperAdmin/importadorSolicitud-SAdmin";
    }

    @GetMapping("/importadorActivos")
    public String viewImportadorActivos(Model model) {
        //Listar usuarios con rol 4, isActivo 1, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolIdrolIdAndIsActivated(4, (byte) 1);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/importadorActivos-SAdmin";
    }

    @GetMapping("/importadorInactivos")
    public String viewImportadorInactivos(Model model) {
        //Listar usuarios con rol 4, isActivo 0, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolIdrolIdAndIsActivated(4, (byte) 0);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/importadorInactivos-SAdmin";
    }

    @GetMapping("/importadorBorrar")
    public String viewImportadorBorrar(Integer id) {
        //Borrado lógico del proveedor sin query method
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setIsActivated((byte) 0);
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/importadorActivos";
    }
    @GetMapping("/importadorReactivar")
    public String viewImportadorReactivar(Integer id) {
        //Reactivar usuario sin query method
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setIsActivated((byte) 1);
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/importadorInactivos";
    }

    @GetMapping("/importadorAceptado")
    public String viewImportadorAceptado(Integer id) {
        //Actualizar usuario a Importador sin query method
        Usuario usuario = usuarioRepository.findById(id).get();
        usuario.setIsAccepted((byte) 1);
        usuario.setIsActivated((byte) 1);
        //Asignar una contraseña por random de 10 dígitos y que combine número y letras
        String password = PasswordGenerator.generateRandomPassword();
        System.out.println(password);
        //Encriptar la contraseña con BCrypt de 10 rondas
        String passwordEncrypted = BCrypt.hashpw(password, BCrypt.gensalt(10));
        usuario.setContrasena(passwordEncrypted);
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/importadorSolicitud";
    }
    @GetMapping("/importadorRechazado")
    public String viewImportadorRechazado(Integer id) {
        //Borrar la solicitud sin query method (Borrado total de la BD)
        usuarioRepository.deleteById(id);
        return "redirect:/superadmin/importadorSolicitud";
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Productos (Completo - falta)">
    @GetMapping("/productoNuevoForm")
    public String viewProductoNuevoForm(Model model) {
        //Enviar un producto
        FormProducto formProducto = new FormProducto();
        model.addAttribute("formProducto", formProducto);
        //Enviar subcategorias
        model.addAttribute("subcategorias", subCategoriaRepository.findAll());
        //Enviar Proveedores
        model.addAttribute("proveedores", proveedorRepository.findAll());

        return "SuperAdmin/productoNuevo-SAdmin";
    }
    @PostMapping("/productoNuevo")
    public String viewProductoNuevo(
            @ModelAttribute FormProducto formProducto,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            RedirectAttributes redirectAttributes) {

        // 1. Obtener el Producto desde el formulario
        Producto producto = formProducto.getProducto();
        productosRepository.save(producto);

        producto.setCodigo(ProductCodeGenerator.generateProductCode(producto));
        //Guardar el producto
        productosRepository.save(producto);
        // 2. Asignación del valor inicial para la foto del producto (valor predeterminado)
        producto.setFoto("default.jpg");  // Establecer la foto predeterminada
        productosRepository.save(producto);  // Guardar el producto para generar su ID

        // 3. Subir la foto si se ha seleccionado una
        if (foto != null && !foto.isEmpty()) {
            try {
                // Ruta dinámica para guardar las imágenes de productos
                String uploadDir = "uploads/imagenesProductos/";

                // Crear el directorio si no existe
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generar un nombre único para el archivo de la imagen usando el ID del producto y el timestamp
                String nombreArchivo = "producto_" + producto.getId() + "_" + System.currentTimeMillis() + ".jpg"; // Asigna el nombre con el ID del producto y timestamp

                // Guardar el archivo de la foto
                Path path = uploadPath.resolve(nombreArchivo);
                Files.write(path, foto.getBytes());

                // Actualizar la imagen del producto en la base de datos
                producto.setFoto(nombreArchivo);  // Asignar el nombre de la foto al producto
                productosRepository.save(producto);  // Guardar el producto con la foto actualizada

                // Mensaje de éxito
                redirectAttributes.addFlashAttribute("message", "Producto creado exitosamente con la imagen.");
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Error al subir la imagen del producto.");
                return "redirect:/superadmin/productoNuevoForm";  // Redirigir si hay un error al subir la imagen
            }
        } else {
            // Si no se subió una foto, el producto se guarda con la imagen predeterminada ("default.jpg")
            redirectAttributes.addFlashAttribute("message", "Producto creado sin imagen.");
        }

        // 4. Asignación de productos a las zonas
        ProductoEnZona productoEnZonaNorte = formProducto.getProductoEnZonaNorte();
        productoEnZonaNorte.setProductoyZona(producto, zonaRepository.findById(1).get());
        productoEnZonaRepository.save(productoEnZonaNorte);

        ProductoEnZona productoEnZonaSur = formProducto.getProductoEnZonaSur();
        productoEnZonaSur.setProductoyZona(producto, zonaRepository.findById(2).get());
        productoEnZonaRepository.save(productoEnZonaSur);

        ProductoEnZona productoEnZonaEste = formProducto.getProductoEnZonaEste();
        productoEnZonaEste.setProductoyZona(producto, zonaRepository.findById(3).get());
        productoEnZonaRepository.save(productoEnZonaEste);

        ProductoEnZona productoEnZonaOeste = formProducto.getProductoEnZonaOeste();
        productoEnZonaOeste.setProductoyZona(producto, zonaRepository.findById(4).get());
        productoEnZonaRepository.save(productoEnZonaOeste);

        // 5. Redirigir al formulario de nuevo producto
        return "redirect:/superadmin/productoNuevoForm";
    }




    @GetMapping("/productoLista")
    public String viewProductoLista(Model model) {
        List<Producto> productos = productosRepository.findAllByIsDeleted((byte) 0);
        List<ProductoEnZona> productosEnZona = productoEnZonaRepository.findAllByIsDeleted((byte) 0);

        Map<Long, Map<Integer, Map<String, Object>>> productoZonaCantidad = new HashMap<>();
        for (ProductoEnZona pz : productosEnZona) {
            productoZonaCantidad
                    .computeIfAbsent(pz.getId().getProductoIdproducto().longValue(), k -> new HashMap<>())
                    .computeIfAbsent(pz.getId().getZonaIdzona(), k -> new HashMap<>())
                    .put("cantidad", pz.getCantidad());
            productoZonaCantidad
                    .get(pz.getId().getProductoIdproducto().longValue())
                    .get(pz.getId().getZonaIdzona())
                    .put("costoEnvio", pz.getCostoEnvio());
            productoZonaCantidad
                    .get(pz.getId().getProductoIdproducto().longValue())
                    .get(pz.getId().getZonaIdzona())
                    .put("estadoRepo", pz.getEstadoRepo());
        }

        model.addAttribute("productos", productos);
        model.addAttribute("productoZonaCantidad", productoZonaCantidad);
        return "SuperAdmin/productoLista-SAdmin";
    }
    @PostMapping("/productoEditarForm")
    public String viewProductoEditarForm(Model model, Integer id) {
        //Buscar producto por id
        ProductoEnZonaId productoEnZonaIdNorte = new ProductoEnZonaId(id,1);
        ProductoEnZonaId productoEnZonaIdSur = new ProductoEnZonaId(id,2);
        ProductoEnZonaId productoEnZonaIdEste = new ProductoEnZonaId(id,3);
        ProductoEnZonaId productoEnZonaIdOeste = new ProductoEnZonaId(id,4);
        //Buscar producto y producto en zona por id
        Producto producto = productosRepository.findById(id).get();
        ProductoEnZona productoEnZonaNorte = productoEnZonaRepository.findById(productoEnZonaIdNorte).get();
        ProductoEnZona productoEnZonaSur = productoEnZonaRepository.findById(productoEnZonaIdSur).get();
        ProductoEnZona productoEnZonaEste = productoEnZonaRepository.findById(productoEnZonaIdEste).get();
        ProductoEnZona productoEnZonaOeste = productoEnZonaRepository.findById(productoEnZonaIdOeste).get();
        //Nuevo FormProducto
        FormProducto formProducto = new FormProducto();
        formProducto.setProducto(producto);
        formProducto.setProductoEnZonaNorte(productoEnZonaNorte);
        formProducto.setProductoEnZonaSur(productoEnZonaSur);
        formProducto.setProductoEnZonaEste(productoEnZonaEste);
        formProducto.setProductoEnZonaOeste(productoEnZonaOeste);
        //Enviar a la vista
        model.addAttribute("formProducto", formProducto);
        //Enviar subcategorias
        model.addAttribute("subcategorias", subCategoriaRepository.findAll());
        //Enviar Proveedores
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "SuperAdmin/productoEditar-SAdmin";
    }
    @PostMapping("/productoEditar")
    public String viewProductoEditar(FormProducto formProducto,RedirectAttributes redirectAttributes) {
        //Validar que se estén guardando los datos correctos por categoria de producto
        Producto producto = formProducto.getProducto();
        //Los unicos atributos que no deben tener valor son los siguientes: ram, almacenamiento, resolucion, ancho, alto y profundidad
        Boolean guardaRopa = ((producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getId() == 1 || (producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getId() == 2) && producto.getRam().isEmpty() && producto.getAlmacenamiento().isEmpty() && producto.getResolucion().isEmpty() && producto.getAncho().isEmpty() && producto.getAlto().isEmpty() && producto.getProfundidad().isEmpty()));
        //Para muebles deben estar vacíos los campos de talla, material, resolucion, ram y almacenamiento
        Boolean guardaMuebles = (producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getId() == 4 && producto.getTalla().isEmpty() && producto.getResolucion().isEmpty() && producto.getRam().isEmpty() && producto.getAlmacenamiento().isEmpty());
        //Para tecnología deben estar vacíos los siguientes atributos: talla y material
        Boolean guardaTecnologia = (producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getId() == 5 && producto.getTalla().isEmpty());
        System.out.println(formProducto.getProducto());
        System.out.println(formProducto.getProductoEnZonaNorte());
        System.out.println(guardaTecnologia);
        System.out.println(guardaMuebles);
        System.out.println(guardaRopa);
        if(guardaRopa || guardaMuebles || guardaTecnologia){

            //Actualizar producto
            Producto productoEdit = formProducto.getProducto();
            productosRepository.save(productoEdit);
            //Determinar los ProductoZonaID
            formProducto.getProductoEnZonaNorte().setProductoyZona(productoEdit,zonaRepository.findById(1).get());
            formProducto.getProductoEnZonaSur().setProductoyZona(productoEdit,zonaRepository.findById(2).get());
            formProducto.getProductoEnZonaEste().setProductoyZona(productoEdit,zonaRepository.findById(3).get());
            formProducto.getProductoEnZonaOeste().setProductoyZona(productoEdit,zonaRepository.findById(4).get());

            if (formProducto.getProductoEnZonaNorte().getCantidad() >= 25) {
                formProducto.getProductoEnZonaNorte().setEstadoRepo((byte) 0); // Stock adecuado
            }
            if (formProducto.getProductoEnZonaSur().getCantidad() >= 25) {
                formProducto.getProductoEnZonaSur().setEstadoRepo((byte) 0); // Stock adecuado
            }
            if (formProducto.getProductoEnZonaEste().getCantidad() >= 25) {
                formProducto.getProductoEnZonaEste().setEstadoRepo((byte) 0); // Stock adecuado
            }
            if (formProducto.getProductoEnZonaSur().getCantidad() >= 25) {
                formProducto.getProductoEnZonaSur().setEstadoRepo((byte) 0); // Stock adecuado
            }
            //Guardar los productos en zona
            productoEnZonaRepository.save(formProducto.getProductoEnZonaNorte());
            productoEnZonaRepository.save(formProducto.getProductoEnZonaSur());
            productoEnZonaRepository.save(formProducto.getProductoEnZonaEste());
            productoEnZonaRepository.save(formProducto.getProductoEnZonaOeste());

            //Enviar mensaje de éxito como redirect attribute
            redirectAttributes.addFlashAttribute("successMessage", "Producto editado correctamente.");
            return "redirect:/superadmin/productoLista";
        }else{
            //Enviar mensaje de error como redirect attribute
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede guardar el producto porque los atributos no coinciden con la categoría.");
            return "redirect:/superadmin/productoLista";
        }
    }
    @GetMapping("/productoBorrar")
    public String viewProductoBorrar(Integer id,RedirectAttributes redirectAttributes) {
        Producto producto = productosRepository.findById(id).get();
        //Buscar el producto en las zonas
        ProductoEnZona productoEnZonaNorte = productoEnZonaRepository.findById(new ProductoEnZonaId(producto.getId(), 1)).get();
        ProductoEnZona productoEnZonaSur = productoEnZonaRepository.findById(new ProductoEnZonaId(producto.getId(), 2)).get();
        ProductoEnZona productoEnZonaEste = productoEnZonaRepository.findById(new ProductoEnZonaId(producto.getId(), 3)).get();
        ProductoEnZona productoEnZonaOeste = productoEnZonaRepository.findById(new ProductoEnZonaId(producto.getId(), 4)).get();
        //Borrado lógico del producto si la contidad en cada zona es 0
        if (productoEnZonaNorte.getCantidad() == 0 && productoEnZonaSur.getCantidad() == 0 && productoEnZonaEste.getCantidad() == 0 && productoEnZonaOeste.getCantidad() == 0) {
            producto.setIsDeleted((byte) 1);
            productoEnZonaNorte.setIsDeleted((byte) 1);
            productoEnZonaSur.setIsDeleted((byte) 1);
            productoEnZonaEste.setIsDeleted((byte) 1);
            productoEnZonaOeste.setIsDeleted((byte) 1);

            productosRepository.save(producto);
            productoEnZonaRepository.save(productoEnZonaNorte);
            productoEnZonaRepository.save(productoEnZonaSur);
            productoEnZonaRepository.save(productoEnZonaEste);
            productoEnZonaRepository.save(productoEnZonaOeste);

            //Enviar mensaje de éxito como redirect attribute
            redirectAttributes.addFlashAttribute("successMessage", "Producto borrado correctamente.");
            return "redirect:/superadmin/productoLista";
        }else{
            //Enviar mensaje de error como redirect attribute
            redirectAttributes.addFlashAttribute("errorMessage", "No se puede borrar el producto porque tiene stock en alguna zona.");
            return "redirect:/superadmin/productoLista";
        }
    }

    //</editor-fold>

    //<editor-fold desc="CRUD Proveedores (Completo - terminado)">
    //<editor-fold desc="Nuevo Proveedor">
    @GetMapping("/proveedorNuevoForm")
    public String viewProveedorNuevoForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "SuperAdmin/proveedorNuevo-SAdmin";
    }

    @PostMapping("/proveedorNuevo")
    public String viewProveedorNuevo(Proveedor proveedor) {
        //Guardar proveedor
        proveedorRepository.save(proveedor);
        return "redirect:/superadmin/proveedorNuevoForm";
    }
    //</editor-fold>
    //<editor-fold desc="Lista Proveedores">
    @GetMapping("/proveedorLista")
    public String viewProveedorLista(Model model) {
        //Lista de proveedores activos
        List<Proveedor> proveedores = proveedorRepository.findAllByIsDeleted((byte) 0);
        //Enviar a la vista
        model.addAttribute("proveedores", proveedores);
        return "SuperAdmin/proveedorLista-SAdmin";
    }
    //</editor-fold>
    //<editor-fold desc="Editar Proveedor">
    @PostMapping("/proveedorEditarForm")
    public String viewProveedorEditarForm(Model model, Integer id) {
        //Buscar proveedor por id
        Proveedor proveedor = proveedorRepository.buscarProveedorPorID(id);
        //Enviar a la vista
        model.addAttribute("proveedor", proveedor);
        return "SuperAdmin/proveedorEditar-SAdmin";
    }

    @PostMapping("/proveedorEditar")
    public String viewProveedorEditar(Proveedor proveedor) {
        //Actualizar proveedor existente
        proveedorRepository.updateProveedor(proveedor.getTelefono(),proveedor.getTienda(), proveedor.getId());
        return "redirect:/superadmin/proveedorLista";
    }
    //</editor-fold>
    //<editor-fold desc="Borrar Proveedor">
    @GetMapping("/proveedorBorrar")
    public String viewProveedorBorrar(Integer id) {
        //Borrado lógico del proveedor
        proveedorRepository.deleteProveedor(id);
        return "redirect:/superadmin/proveedorLista";
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="Ayuda">
    @GetMapping("/ayuda")
    public String viewAyuda() {
        return "SuperAdmin/ayuda-SAdmin";
    }
    //</editor-fold>
}
