package com.app.tradogt.repository;

import com.app.tradogt.entity.Notificacion;
import com.app.tradogt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    // Método para encontrar notificaciones no leídas para un usuario específico, ordenadas por fecha de creación
    //List<Notificacion> findByUsuarioAndLeidoFalseOrderByFechaCreacionAsc(Usuario usuario);

    // Método para encontrar todas las notificaciones para un usuario específico, ordenadas por fecha de creación
    List<Notificacion> findByUsuarioOrderByFechaCreacionAsc(Usuario usuario);


    // Método para encontrar todas las notificaciones de un usuario
    List<Notificacion> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);

    //Metodo para limitar las notificacions por usuario
    @Query(value = "select * from Notificacion where Notificacion.idUsuarioNoti = :idUser", nativeQuery = true)
    List<Notificacion> findByUsuarioNoti(@Param("idUser") int idUser);


}
