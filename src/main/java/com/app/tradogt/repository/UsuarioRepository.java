package com.app.tradogt.repository;

import com.app.tradogt.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //Buscar por id
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    Usuario findByIdUsuario(Integer id);
    //Borrado lógico por ID (isActive = 0)
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
}
