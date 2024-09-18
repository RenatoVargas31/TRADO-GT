package com.app.tradogt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/superadmin")
public class SuperAdminController {
    @GetMapping("/inicio")
    public String viewInicio() {
        return "SuperAdmin/inicio-SAdmin";
    }

    @GetMapping("/dashboard")
    public String viewDashboard() {
        return "SuperAdmin/dashboard-SAdmin";
    }

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

    @GetMapping("/proveedorNuevo")
    public String viewProveedorNuevo() {
        return "SuperAdmin/proveedorNuevo-SAdmin";
    }
    @GetMapping("/proveedorEditar")
    public String viewProveedorEditar() {
        return "SuperAdmin/proveedorEditar-SAdmin";
    }

    @GetMapping("/proveedorLista")
    public String viewProveedorLista() {
        return "SuperAdmin/proveedorLista-SAdmin";
    }
    @GetMapping("/ayuda")
    public String viewAyuda() {
        return "SuperAdmin/ayuda-SAdmin";
    }
}
