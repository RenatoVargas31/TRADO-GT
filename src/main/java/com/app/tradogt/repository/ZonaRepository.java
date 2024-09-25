package com.app.tradogt.repository;

import com.app.tradogt.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {
    //<editor-fold desc="CRUD Zona">
    //</editor-fold>
}
