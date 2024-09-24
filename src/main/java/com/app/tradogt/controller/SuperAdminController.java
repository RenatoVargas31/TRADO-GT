package com.app.tradogt.controller;

import com.app.tradogt.entity.Proveedor;
import com.app.tradogt.repository.ProveedorRepository;
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

    public SuperAdminController(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
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

    //<editor-fold desc="CRUD Administrador Zonal (Completo - falta)">
    @GetMapping("/admZonalNuevo")
    public String viewAdmZonalNuevo() {
        return "SuperAdmin/admZonalNuevo-SAdmin";
    }

    @GetMapping("/admZonalEditar")
    public String viewAdmZonalEditar() {
        return "SuperAdmin/admZonalEditar-SAdmin";
    }

    @GetMapping("/admZonalActivos")
    public String viewAdmZonalActivos() {
        return "SuperAdmin/admZonalActivos-SAdmin";
    }

    @GetMapping("/admZonalInactivos")
    public String viewAdmZonalInactivos() {
        return "SuperAdmin/admZonalInactivos-SAdmin";
    }
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
    //Funcionalidad de nuevo que recibe el objeto proveedor y lo guarda
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
