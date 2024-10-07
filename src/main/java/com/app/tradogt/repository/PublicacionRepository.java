package com.app.tradogt.repository;
import com.app.tradogt.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

    @Query(value = "SELECT p.idPublicacion, p.titulo, p.cuerpo, p.fechaCreacion, u.nombre, u.apellido " +
            "FROM Publicacion p " +
            "INNER JOIN Usuario u ON p.Usuario_idUsuario = u.idUsuario " +
            "WHERE u.rol_idRol = 4 " +
            "AND u.isAccepted = 1 " +
            "AND u.isPostulated = 0 " +
            "AND u.isActivated = 1", nativeQuery = true)
    List<Object[]> findPublicacionesUsuariosNoBaneados();
}
