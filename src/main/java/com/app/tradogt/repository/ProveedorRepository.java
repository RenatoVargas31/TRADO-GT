package com.app.tradogt.repository;

import com.app.tradogt.entity.Proveedor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {


    //<editor-fold desc="Buscar por Enabled">
    List<Proveedor> findAllByEnabled(Byte enabled);
    //</editor-fold>

    //<editor-fold desc="Buscar por ID">
    @Query("SELECT p FROM Proveedor p WHERE p.id = :id")
    Proveedor findByIdProveedor(Integer id);
    //</editor-fold>

    //<editor-fold desc="Actualizar Proveedor">
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.telefonoProveedor = :telefonoProveedor, p.nombreTienda = :nombreTienda WHERE p.id = :id")
    void updateProveedor(String telefonoProveedor, String nombreTienda, Integer id);
    //</editor-fold>

    //<editor-fold desc="Borrar Proveedor">
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.enabled = 0 WHERE p.id = :id")
    void deleteProveedor(Integer id);
    //</editor-fold>
    //</editor-fold>

}
