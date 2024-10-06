package com.app.tradogt.repository;

import com.app.tradogt.entity.ProductoEnZonaEnOrden;
import com.app.tradogt.entity.ProductoEnZonaEnOrdenId;
import com.app.tradogt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoEnZonaEnOrdenRepository extends JpaRepository<ProductoEnZonaEnOrden, ProductoEnZonaEnOrdenId> {


    List<ProductoEnZonaEnOrden> findByordenIdordenId(Integer id);

    Usuario findByOrdenIdorden_UsuarioIdusuario_Id(Integer id);
}
