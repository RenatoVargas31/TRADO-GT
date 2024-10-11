package com.app.tradogt.controller;

import com.app.tradogt.entity.*;
import com.app.tradogt.helpers.PasswordGenerator;
import com.app.tradogt.helpers.ProductCodeGenerator;
import com.app.tradogt.repository.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/superadmin")
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

    public SuperAdminController(ProveedorRepository proveedorRepository, UsuarioRepository usuarioRepository, ZonaRepository zonaRepository, RolRepository rolRepository, DistritoRepository distritoRepository, ProductosRepository productosRepository, ProductoEnZonaRepository productoEnZonaRepository, SubCategoriaRepository subCategoriaRepository, CategoriaRepository categoriaRepository) {
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


    //<editor-fold desc="CRUD Menu (R - falta)">
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
    public String viewDashboard() {
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
    public String viewProductoNuevo(@ModelAttribute FormProducto formProducto) {
        // Crear una instancia de Producto
        Producto producto = formProducto.getProducto();
        System.out.println(producto);
        // Guardar el producto
        productosRepository.save(producto);
        //Genrar un código de producto con ProductCodeGenerator
        producto.setCodigo(ProductCodeGenerator.generateProductCode(producto));
        //Guardar el producto
        productosRepository.save(producto);
        //Crear instancias de ProductoEnZona
        //Norte
        ProductoEnZona productoEnZonaNorte = formProducto.getProductoEnZonaNorte();
        productoEnZonaNorte.setProductoyZona(producto,zonaRepository.findById(1).get());
        productoEnZonaRepository.save(productoEnZonaNorte);
        //Sur
        ProductoEnZona productoEnZonaSur = formProducto.getProductoEnZonaSur();
        productoEnZonaSur.setProductoyZona(producto,zonaRepository.findById(2).get());
        productoEnZonaRepository.save(productoEnZonaSur);
        //Este
        ProductoEnZona productoEnZonaEste = formProducto.getProductoEnZonaEste();
        productoEnZonaEste.setProductoyZona(producto,zonaRepository.findById(3).get());
        productoEnZonaRepository.save(productoEnZonaEste);
        //Oeste
        ProductoEnZona productoEnZonaOeste = formProducto.getProductoEnZonaOeste();
        productoEnZonaOeste.setProductoyZona(producto,zonaRepository.findById(4).get());
        productoEnZonaRepository.save(productoEnZonaOeste);

        return "redirect:/superadmin/productoNuevoForm";
    }
    @GetMapping("/productoEditar")
    public String viewProductoEditar() {
        return "SuperAdmin/productoEditar-SAdmin";
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
        }

        model.addAttribute("productos", productos);
        model.addAttribute("productoZonaCantidad", productoZonaCantidad);
        return "SuperAdmin/productoLista-SAdmin";
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
