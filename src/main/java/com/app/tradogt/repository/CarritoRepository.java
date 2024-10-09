package com.app.tradogt.repository;

import com.app.tradogt.entity.Carrito;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    /*
    List<Carrito> findByOrdenIdordenAndUsuarioIdusuario(Orden ordenIdorden, Usuario usuario);

    List<Carrito> findByOrdenIdorden(Orden ord);
*/
}
