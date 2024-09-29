package com.app.tradogt.repository;

import com.app.tradogt.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductosRepository extends JpaRepository<Producto, Integer> {
}
