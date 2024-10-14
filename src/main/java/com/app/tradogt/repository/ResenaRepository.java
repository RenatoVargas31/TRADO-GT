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

    // Filtrar reseñas de usuarios que no están baneados
    List<Resena> findByUsuarioIdusuarioIsAcceptedAndUsuarioIdusuarioIsPostulatedAndUsuarioIdusuarioIsActivated(
            int isAccepted, int isPostulated, int isActivated);

    @Query("SELECT COALESCE(ROUND(AVG(r.calificacion), 1), 0) FROM Resena r WHERE r.productoIdproducto.id = :id")
    Integer findRating(@Param("id") int id);


    @Query("SELECT COUNT(r) FROM Resena r WHERE r.productoIdproducto.id = :id")
    Integer countResena(@Param("id") int id);





}
