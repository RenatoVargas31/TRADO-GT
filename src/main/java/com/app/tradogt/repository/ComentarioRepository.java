package com.app.tradogt.repository;


import com.app.tradogt.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {
    @Query("SELECT c FROM Comentario c WHERE c.publicacionIdpublicacion.id = :publicacionId")
    List<Comentario> findByPublicacionId(Integer publicacionId);
}
