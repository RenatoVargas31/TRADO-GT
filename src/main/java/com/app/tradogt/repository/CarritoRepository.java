package com.app.tradogt.repository;

import com.app.tradogt.entity.Carrito;
import com.app.tradogt.entity.CarritoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, CarritoId> {
}
