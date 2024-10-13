package com.app.tradogt.repository;

import com.app.tradogt.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, Integer> {
    List<Distrito> findByZonaIdzonaId(int id);
    Optional<Distrito> findByNombre(String nombre);
}