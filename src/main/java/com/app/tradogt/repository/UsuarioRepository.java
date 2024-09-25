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

    //</editor-fold>

    //<editor-fold desc="CRUD Importador">
    //Método pra actualizar el id rol de un usuario a 3 y el postulaAgente a 0
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.rolesIdroles.id = 3, u.postulaAgente = 0 WHERE u.id = :id")
    void updateImportadorPostulanteApto(Integer id);
    //Método para actualizar el postulaAgente a 0
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.postulaAgente = 0 WHERE u.id = :id")
    void updateImportadorPostulanteNoApto(Integer id);
    //Método para actualizar el isAccepted a 1 y isActive a 1
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.isAccepted = 1, u.isActive = 1 WHERE u.id = :id")
    void updateImportadorAceptado(Integer id);
    //Método para borrado total del importador
    @Modifying
    @Transactional
    @Query("DELETE FROM Usuario u WHERE u.id = :id")
    void deleteImportadorRechazado(Integer id);
    //</editor-fold>
}
