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

    // Si necesitas filtrar reseñas por producto o usuario, podrías agregar más métodos
    List<Resena> findByUsuarioIdusuario(Usuario usuario);

    List<Resena> findByProductoIdproducto(Producto producto);
}
