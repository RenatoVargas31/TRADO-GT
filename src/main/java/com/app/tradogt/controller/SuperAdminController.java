package com.app.tradogt.controller;

import com.app.tradogt.entity.Proveedor;
import com.app.tradogt.entity.Usuario;
import com.app.tradogt.helpers.PasswordGenerator;
import com.app.tradogt.repository.ProveedorRepository;
import com.app.tradogt.repository.RolRepository;
import com.app.tradogt.repository.UsuarioRepository;
import com.app.tradogt.repository.ZonaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {

    //<editor-fold desc="Repositories">
    final ProveedorRepository proveedorRepository;
    final UsuarioRepository usuarioRepository;
    final ZonaRepository zonaRepository;
    final RolRepository rolRepository;

    public SuperAdminController(ProveedorRepository proveedorRepository, UsuarioRepository usuarioRepository, ZonaRepository zonaRepository, RolRepository rolRepository) {
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.zonaRepository = zonaRepository;
        this.rolRepository = rolRepository;
    }
    //</editor-fold>


    //<editor-fold desc="CRUD Menu (R - falta)">
    @GetMapping("/inicio")
    public String viewInicio() {
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
        model.addAttribute("rol", rolRepository.findById(2).orElseThrow());
        return "SuperAdmin/admZonalNuevo-SAdmin";
    }
    @PostMapping("/admZonalNuevo")
    public String viewAdmZonalNuevoForm(@ModelAttribute Usuario usuario) {
        //Asignar una contraseña por random de 10 dígitos y que combine número y letras
        usuario.setContrasenaUsuario(PasswordGenerator.generateRandomPassword());
        //Guardar usuario
        usuarioRepository.save(usuario);
        return "redirect:/superadmin/admZonalNuevoForm";
    }
    //</editor-fold>

    //<editor-fold desc="Editar Administrador Zonal">
    @PostMapping("/admZonalEditarForm")
    public String viewAdmZonalEditarForm(Model model, Integer id) {
        //Buscar proveedor por id
        Usuario usuario = usuarioRepository.findByIdUsuario(id);
        //Enviar a la vista
        model.addAttribute("usuario", usuario);
        //Enviar lista de zonas
        model.addAttribute("zonas", zonaRepository.findAll());
        //Enviar rol de id 2 (Administrador Zonal)
        model.addAttribute("rol", rolRepository.findById(2).orElseThrow());
        return "SuperAdmin/admZonalEditar-SAdmin";
    }
    @PostMapping("/admZonalEditar")
    public String viewAdmZonalEditar(Usuario usuario) {
        //Actualizar usuario existente
        usuarioRepository.updateUsuario(usuario.getCorreoUsuario(), usuario.getTelefonoUsuario(), usuario.getZonasIdzona().getId(), usuario.getId());
        if (usuario.getIsActive() == 1) {
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
        List<Usuario> usuarios = usuarioRepository.findAllByRolesIdrolesIdAndIsActive(2, (byte) 1);
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/admZonalActivos-SAdmin";
    }

    @GetMapping("/admZonalInactivos")
    public String viewAdmZonalInactivos(Model model) {
        //Listar usuarios que tengan id rol 2 (Administrador Zonal) y isActivo 0
        List<Usuario> usuarios = usuarioRepository.findAllByRolesIdrolesIdAndIsActive(2, (byte) 0);
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/admZonalInactivos-SAdmin";
    }
    //</editor-fold>
    //<editor-fold desc="Baneo y desbaneo de AdministradorZonal">
    @GetMapping("/admZonalBorrar")
    public String viewAdmZonalBorrar(Integer id) {
        //Borrado lógico del proveedor
        usuarioRepository.deleteUsuario(id);
        return "redirect:/superadmin/admZonalActivos";
    }
    @GetMapping("/admZonalReactivar")
    public String viewAdmZonalReactivar(Integer id) {
        //Reactivar usuario
        usuarioRepository.reactivateUsuario(id);
        return "redirect:/superadmin/admZonalInactivos";
    }
    //</editor-fold>
    //</editor-fold>


    //<editor-fold desc="CRUD Agente de Compra (RUD - falta)">
    @GetMapping("/agentCompraEditar")
    public String viewAgentCompraEditar() {
        return "SuperAdmin/agentCompraEditar-SAdmin";
    }

    @GetMapping("/agentCompraPostula")
    public String viewAgentCompraPostula() {
        return "SuperAdmin/agentCompraPostula-SAdmin";
    }

    @GetMapping("/agentCompraActivos")
    public String viewAgentCompraActivos() {
        return "SuperAdmin/agentCompraActivos-SAdmin";
    }

    @GetMapping("/agentCompraInactivos")
    public String viewAgentCompraInactivos() {
        return "SuperAdmin/agentCompraInactivos-SAdmin";
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Importador (RUD - falta)">
    @GetMapping("/importadorSolicitud")
    public String viewImportdorSolicitud() {
        return "SuperAdmin/importadorSolicitud-SAdmin";
    }

    @GetMapping("/importadorEditar")
    public String viewImportadorEditar() {
        return "SuperAdmin/importadorEditar-SAdmin";
    }

    @GetMapping("/importadorActivos")
    public String viewImportadorActivos() {
        return "SuperAdmin/importadorActivos-SAdmin";
    }
    @GetMapping("/importadorInactivos")
    public String viewImportadorInactivos() {
        return "SuperAdmin/importadorInactivos-SAdmin";
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Productos (Completo - falta)">
    @GetMapping("/productoNuevo")
    public String viewProductoNuevo(Model model) {
        return "SuperAdmin/productoNuevo-SAdmin";
    }
    @GetMapping("/productoEditar")
    public String viewProductoEditar() {
        return "SuperAdmin/productoEditar-SAdmin";
    }
    @GetMapping("/productoLista")
    public String viewProductoLista() {
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
        List<Proveedor> proveedores = proveedorRepository.findAllByEnabled((byte) 1);
        //Enviar a la vista
        model.addAttribute("proveedores", proveedores);
        return "SuperAdmin/proveedorLista-SAdmin";
    }
    //</editor-fold>
    //<editor-fold desc="Editar Proveedor">
    @PostMapping("/proveedorEditarForm")
    public String viewProveedorEditarForm(Model model, Integer id) {
        //Buscar proveedor por id
        Proveedor proveedor = proveedorRepository.findByIdProveedor(id);
        //Enviar a la vista
        model.addAttribute("proveedor", proveedor);
        return "SuperAdmin/proveedorEditar-SAdmin";
    }

    @PostMapping("/proveedorEditar")
    public String viewProveedorEditar(Proveedor proveedor) {
        //Actualizar proveedor existente
        proveedorRepository.updateProveedor(proveedor.getTelefonoProveedor(),proveedor.getNombreTienda(), proveedor.getId());
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
