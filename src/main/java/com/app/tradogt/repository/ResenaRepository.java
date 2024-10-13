package com.app.tradogt.repository;


import com.app.tradogt.entity.Producto;
import com.app.tradogt.entity.Resena;
import com.app.tradogt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    List<Resena> findAll();

    // Filtrar reseñas de usuarios que no están baneados
    List<Resena> findByUsuarioIdusuarioIsAcceptedAndUsuarioIdusuarioIsPostulatedAndUsuarioIdusuarioIsActivated(
            int isAccepted, int isPostulated, int isActivated);
}
