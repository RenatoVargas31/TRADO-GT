package com.app.tradogt.repository;

import com.app.tradogt.entity.Carrito;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    Carrito findByusuarioIdusuarioAndIsDelete(Usuario usuario, byte b);
    Carrito findByUsuarioIdusuarioAndIsDelete(Usuario usuario, byte b);
}
