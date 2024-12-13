package com.app.tradogt.repository;


import com.app.tradogt.entity.Producto;
import com.app.tradogt.entity.Resena;
import com.app.tradogt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {

    List<Resena> findAll();

    // Filtrar resenas de usuarios que no están baneados
    List<Resena> findByUsuarioIdusuarioIsAcceptedAndUsuarioIdusuarioIsPostulatedAndUsuarioIdusuarioIsActivated(
            int isAccepted, int isPostulated, int isActivated);

    @Query("SELECT COALESCE(ROUND(AVG(r.calificacion), 1), 0) FROM Resena r WHERE r.productoIdproducto.id = :id")
    Double findRating(@Param("id") int id);


    @Query("SELECT COUNT(r) FROM Resena r WHERE r.productoIdproducto.id = :id")
    Integer countResena(@Param("id") int id);

    @Query(value = "SELECT r.titulo as Titulo, " +
            "r.cuerpo as Cuerpo, " +
            "r.calificacion as Calificación, " +
            "r.fechaCreacion as Fecha, " +
            "CONCAT(u.nombre, ' ', u.apellido) AS NombreCompleto, " +
            "u.idUsuario as Id " +
            "FROM Resena r " +
            "JOIN Usuario u ON u.idUsuario = r.Usuario_idUsuario " +
            "WHERE r.Producto_idProducto = :id " +
            "ORDER BY r.fechaCreacion DESC " +
            "LIMIT 4",
            nativeQuery = true)
    List<Object[]> comentarioProducto(@Param("id") Integer id);



}
