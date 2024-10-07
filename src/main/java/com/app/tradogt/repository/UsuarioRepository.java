package com.app.tradogt.repository;

import com.app.tradogt.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.data.jpa.repository.*;


import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //Buscar usuarios activos
    List<Usuario> findByIsActivatedAndAgentcompraIdusuario_Id(Byte isActivated, Integer idAgente);

    //Buscar usuarios asignados a determinado agente
    List<Usuario> findByAgentcompraIdusuario_Id(Integer idAgente);


    /*
    @Query(value = "SELECT u.id, u.nombre, u.apellido, p.nombre , c.nombre , eoi.nombre, pzo.cantidad ,a.nombre, a.apellido " +
            "FROM Usuario u " +
            "LEFT JOIN u.agentcompraIdusuario a " +
            "INNER JOIN Orden o ON o.usuarioIdusuario.id = u.id " +
            "INNER JOIN o.estadoordenimportadorIdestadoordenimportador eoi " +
            "INNER JOIN ProductoEnZonaEnOrden pzo ON pzo.ordenIdorden.id = o.id " +
            "INNER JOIN ProductoEnZona pz ON pz.productoIdproducto = pzo.productoEnZona.productoIdproducto " +
            "INNER JOIN pz.productoIdproducto p " + // Aquí se accede al producto a través de la relación
            "INNER JOIN p.subcategoriaIdsubcategoria sc " +
            "INNER JOIN sc.categoriaIdcategoria c " +
            "WHERE u.rolIdrol.id = 4")
    List<Object[]> getUsuarioOrderProductDetails();
    */



    @Query(value = """
        SELECT 
            a.idUsuario AS AgenteID,
            a.Nombre AS AgenteNombre,
            a.Apellido AS AgenteApellido,
            az.Nombre AS AdminZonalNombre,
            az.Apellido AS AdminZonalApellido,
            d.Nombre AS DistritoNombre,
            (SELECT COUNT(u.idUsuario) FROM usuario u WHERE u.AgentCompra_idUsuario = a.idUsuario AND u.Rol_idRol = 4) AS NumeroDeUsuarios,
            (SELECT AVG(o.Valoracion_idValoracion) FROM Orden o WHERE o.AgentCompra_idUsuario = a.idUsuario) AS ValoracionAgente,
            (SELECT COUNT(o.idOrden) FROM Orden o WHERE o.AgentCompra_idUsuario = a.idUsuario AND o.EstadoOrdenAgente_idEstadoOrdenAgente = 5) AS Finalizados
        FROM 
            usuario a
        LEFT JOIN 
            usuario az ON a.AdmZonal_idUsuario = az.idUsuario
        INNER JOIN 
            Distrito d ON d.idDistrito = a.Distrito_idDistrito
        INNER JOIN 
            Orden o ON o.AgentCompra_idUsuario = a.idUsuario
        WHERE 
            az.idUsuario = 3
    """, nativeQuery = true)
    List<Object[]> getAgenteDetailsNative();


    @Query(value = """
        SELECT 
            a.idUsuario AS AgenteID,
            a.Nombre AS AgenteNombre,
            a.Apellido AS AgenteApellido,
            a.Dni AS AgenteDni,
            a.Correo AS AgenteCorreo,
            a.Telefono AS AgenteTelefono,
            z.Nombre AS ZonaNombre,
            d.Nombre AS DistritoNombre,
            a.Direccion AS AgenteDireccion,
            cd.Caracteres AS CaracteresDespachador,
            a.Ruc AS AgenteRUC,
            a.RazonSocial AS RazonSocial
        FROM 
            usuario a
        INNER JOIN 
            Zona z ON z.idZona = a.Zona_idZona
        INNER JOIN 
            Distrito d ON d.idDistrito = a.Distrito_idDistrito
        INNER JOIN 
            codigodespachador cd ON cd.Distrito_idDistrito = d.idDistrito
        WHERE 
            a.idUsuario = :usuarioId
    """, nativeQuery = true)
    List<Object[]> getAgenteDetailsById(@Param("usuarioId") Integer usuarioId);

    //Buscar por ID
    //Buscar por Rol y activo
    List<Usuario> findAllByRolIdrolIdAndIsAccepted(Integer idRol, Byte isAccepted);

    List<Usuario> findAllByIsAccepted(Byte isAccepted);

    List<Usuario> findAllByRolIdrolIdAndIsActivated(Integer idRol, Byte isActivated);

    List<Usuario> findAllByRolIdrolIdAndIsPostulated(Integer idRol, Byte isPostulated);

    List<Usuario> findAllByRolIdrolIdAndIsActivatedAndZonaIdzonaId(Integer idRol, Byte isActivated, Integer idZona);

    List<Usuario> findAllByAdmzonalIdusuario_IdAndIsActivated(Integer idAdmZonal, Byte isActivated);


    //Buscar por id
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    Usuario findByIdUsuario(Integer id);
    //Borrado lógico por ID (isActive = 0)
    /*
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.isActive = 0 WHERE u.id = :id")
    void deleteUsuario(Integer id);
    //Reactivar por id (isActive = 1)
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.isActive = 1 WHERE u.id = :id")
    void reactivateUsuario(Integer id);
    //Buscar por rol y activo
    List<Usuario> findAllByRolesIdrolesIdAndIsActive(Integer idroles, Byte isActive);
    List<Usuario> findAllByRolesIdrolesIdAndIsActiveAndIsAccepted(Integer idroles, Byte isActive, Byte isAccepted);
    List<Usuario> findAllByRolesIdrolesIdAndPostulaAgenteAndIsActive(Integer idroles, Byte postula, Byte isActive);
    List<Usuario> findAllByRolesIdrolesIdAndIsAccepted(Integer idroles, Byte isAccepted);
    //<editor-fold desc="CRUD Administrador Zonal">
    //Actualizar usuario correo, telefono y zona
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.correoUsuario = :correoUsuario, u.telefonoUsuario = :telefonoUsuario, u.zonasIdzona.id= :idZona WHERE u.id = :id")
    void updateAdmZonal(String correoUsuario, String telefonoUsuario, Integer idZona, Integer id);

    //</editor-fold>

    //<editor-fold desc="CRUD Agente de Compra">
    //Actualizar la direccion, telefono, correo, distrito, zona, codigoJurisdiccion, codigoDespachador y razonSocial
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.direccionUsuario = :direccionUsuario, u.telefonoUsuario = :telefonoUsuario, u.correoUsuario = :correoUsuario, u.distritosIddistrito.id = :idDistrito, u.zonasIdzona.id = :idZona, u.codigoJurisdiccion = :codigoJurisdiccion, u.codigoDespachador = :codigoDespachador, u.razonSocial = :razonSocial WHERE u.id = :id")
    void updateAgenteCompra(String direccionUsuario, String telefonoUsuario, String correoUsuario, Integer idDistrito, Integer idZona, String codigoJurisdiccion, String codigoDespachador, String razonSocial, Integer id);

    //</editor-fold>

    //<editor-fold desc="CRUD Importador">
    //Actualizar el id rol de un usuario a 3 y el postulaAgente a 0
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.rolesIdroles.id = 3, u.postulaAgente = 0 WHERE u.id = :id")
    void updateImportadorPostulanteApto(Integer id);
    //Actualizar el postulaAgente a 0
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.postulaAgente = 0 WHERE u.id = :id")
    void updateImportadorPostulanteNoApto(Integer id);
    //Actualizar el isAccepted a 1 y isActive a 1
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.isAccepted = 1, u.isActive = 1 WHERE u.id = :id")
    void updateImportadorAceptado(Integer id);
    //Borrado total del importador
    @Modifying
    @Transactional
    @Query("DELETE FROM Usuario u WHERE u.id = :id")
    void deleteImportadorRechazado(Integer id);
    //Actualizar el correo, direccion y distrito
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.correoUsuario = :correoUsuario, u.direccionUsuario = :direccionUsuario, u.distritosIddistrito.id = :idDistrito WHERE u.id = :id")
    void updateImportador(String correoUsuario, String direccionUsuario, Integer idDistrito, Integer id);
    //</editor-fold>
    */
}
