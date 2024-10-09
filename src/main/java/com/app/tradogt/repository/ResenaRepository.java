package com.app.tradogt.repository;


import com.app.tradogt.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    /*
    @Query("SELECT r FROM Resena r WHERE r.carrito.usuarioIdusuario.isAccepted = 1 AND r.carrito.usuarioIdusuario.isPostulated = 0 AND r.carrito.usuarioIdusuario.isActivated = 1 AND r.carrito.usuarioIdusuario.rolIdrol.id = 4")
    List<Resena> findResenasUsuariosValidos();
    */

}
