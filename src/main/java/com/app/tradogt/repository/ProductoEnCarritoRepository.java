package com.app.tradogt.repository;

import com.app.tradogt.entity.Carrito;
import com.app.tradogt.entity.ProductoEnCarrito;
import com.app.tradogt.entity.ProductoEnCarritoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoEnCarritoRepository extends JpaRepository<ProductoEnCarrito, ProductoEnCarritoId> {

    List<ProductoEnCarrito> findBycarritoIdcarrito(Carrito carrito);
}
