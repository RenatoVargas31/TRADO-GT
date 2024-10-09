package com.app.tradogt.repository;

import com.app.tradogt.entity.ProductoEnZona;
import com.app.tradogt.entity.ProductoEnZonaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoEnZonaRepository extends JpaRepository<ProductoEnZona, ProductoEnZonaId> {
    /*
    @Modifying
    @Query(value = "UPDATE productoEnZona SET estadoRepo = :nuevoEstado WHERE Producto_idProducto = :idProducto", nativeQuery = true)
    void actualizarEstadoOrden(@Param("nuevoEstado") Byte nuevoEstado, @Param("idProducto") Long idProducto);
    */
    List<ProductoEnZona> findAllByIsDeleted(Byte isDeleted);
}
