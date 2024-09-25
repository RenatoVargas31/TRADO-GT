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
    public String viewInicio(Model model) {
        //Listar usuarios que tenga id rol 2 (Administrador Zonal) y isAccepted 1
        List<Usuario> admZonales = usuarioRepository.findAllByRolesIdrolesIdAndIsAccepted(2, (byte) 1);
        //Listar usuarios que tenga id rol 3 (Agente de Compra) y isAccepted 1
        List<Usuario> agentes = usuarioRepository.findAllByRolesIdrolesIdAndIsAccepted(3, (byte) 1);
        //Listar usuarios que tenga id rol 4 (Importador) y isAccepted 1
        List<Usuario> importadores = usuarioRepository.findAllByRolesIdrolesIdAndIsAccepted(4, (byte) 1);
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
        usuarioRepository.updateAdmZonal(usuario.getCorreoUsuario(), usuario.getTelefonoUsuario(), usuario.getZonasIdzona().getId(), usuario.getId());
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
    public String viewAgentCompraPostula(Model model) {
        List<Usuario> solicitudes = usuarioRepository.findAllByRolesIdrolesIdAndPostulaAgenteAndIsActive(4, (byte) 1, (byte) 1);
        //Enviar a la vista
        model.addAttribute("solicitudes", solicitudes);
        return "SuperAdmin/agentCompraPostula-SAdmin";
    }

    @GetMapping("/agentCompraActivos")
    public String viewAgentCompraActivos(Model model) {
        //Listar usuarios con rol 3, isActivo 1, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolesIdrolesIdAndIsActiveAndIsAccepted(3, (byte) 1, (byte) 1);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/agentCompraActivos-SAdmin";
    }

    @GetMapping("/agentCompraInactivos")
    public String viewAgentCompraInactivos(Model model) {
        //Listar usuarios con rol 3, isActivo 0, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolesIdrolesIdAndIsActiveAndIsAccepted(3, (byte) 0, (byte) 1);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/agentCompraInactivos-SAdmin";
    }
    @GetMapping("/agentCompraBorrar")
    public String viewAgentCompraBorrar(Integer id) {
        //Borrado lógico del proveedor
        usuarioRepository.deleteUsuario(id);
        return "redirect:/superadmin/agentCompraActivos";
    }
    @GetMapping("/agentCompraReactivar")
    public String viewAgentCompraReactivar(Integer id) {
        //Reactivar usuario
        usuarioRepository.reactivateUsuario(id);
        return "redirect:/superadmin/agentCompraInactivos";
    }
    @GetMapping("/agentCompraPostulanteApto")
    public String viewAgentCompraPostulanteApto(Integer id) {
        //Actualizar usuario a Agente de Compra
        usuarioRepository.updateImportadorPostulanteApto(id);
        return "redirect:/superadmin/agentCompraPostula";
    }
    @GetMapping("/agentCompraPostulanteNoApto")
    public String viewAgentCompraPostulanteNoApto(Integer id) {
        //Borrar la postulación
        usuarioRepository.updateImportadorPostulanteNoApto(id);
        return "redirect:/superadmin/agentCompraPostula";
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Importador (RUD - falta)">
    @GetMapping("/importadorEditar")
    public String viewImportadorEditar() {
        return "SuperAdmin/importadorEditar-SAdmin";
    }

    @GetMapping("/importadorSolicitud")
    public String viewImportadorSolicitud(Model model) {
        //Listar usuarios con rol 4, isActivo 0, isAceptado 0
        List<Usuario> solicitudes = usuarioRepository.findAllByRolesIdrolesIdAndIsActiveAndIsAccepted(4, (byte) 0, (byte) 0);
        //Enviar a la vista
        model.addAttribute("solicitudes", solicitudes);
        return "SuperAdmin/importadorSolicitud-SAdmin";
    }

    @GetMapping("/importadorActivos")
    public String viewImportadorActivos(Model model) {
        //Listar usuarios con rol 4, isActivo 1, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolesIdrolesIdAndIsActiveAndIsAccepted(4, (byte) 1, (byte) 1);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/importadorActivos-SAdmin";
    }
    @GetMapping("/importadorInactivos")
    public String viewImportadorInactivos(Model model) {
        //Listar usuarios con rol 4, isActivo 0, isAceptado 1
        List<Usuario> usuarios = usuarioRepository.findAllByRolesIdrolesIdAndIsActiveAndIsAccepted(4, (byte) 0, (byte) 1);
        //Enviar a la vista
        model.addAttribute("usuarios", usuarios);
        return "SuperAdmin/importadorInactivos-SAdmin";
    }
    @GetMapping("/importadorBorrar")
    public String viewImportadorBorrar(Integer id) {
        //Borrado lógico del proveedor
        usuarioRepository.deleteUsuario(id);
        return "redirect:/superadmin/importadorActivos";
    }
    @GetMapping("/importadorReactivar")
    public String viewImportadorReactivar(Integer id) {
        //Reactivar usuario
        usuarioRepository.reactivateUsuario(id);
        return "redirect:/superadmin/importadorInactivos";
    }
    @GetMapping("/importadorAceptado")
    public String viewImportadorAceptado(Integer id) {
        //Actualizar usuario a Importador
        usuarioRepository.updateImportadorAceptado(id);
        return "redirect:/superadmin/importadorSolicitud";
    }
    @GetMapping("/importadorRechazado")
    public String viewImportadorRechazado(Integer id) {
        //Borrar la solicitud
        usuarioRepository.deleteImportadorRechazado(id);
        return "redirect:/superadmin/importadorSolicitud";
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Productos (Completo - falta)">
    @GetMapping("/productoNuevo")
    public String viewProductoNuevo() {
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
