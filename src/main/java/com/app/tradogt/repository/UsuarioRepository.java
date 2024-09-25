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

    //<editor-fold desc="CRUD Administrador Zonal">
    //Actualizar usuario correo, telefono y zona
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.correoUsuario = :correoUsuario, u.telefonoUsuario = :telefonoUsuario, u.zonasIdzona.id= :idZona WHERE u.id = :id")
    void updateUsuario(String correoUsuario, String telefonoUsuario, Integer idZona, Integer id);
    //</editor-fold>

    //<editor-fold desc="CRUD Agente de Compra">

    //</editor-fold>

    //<editor-fold desc="CRUD Importador">

    //</editor-fold>
}
