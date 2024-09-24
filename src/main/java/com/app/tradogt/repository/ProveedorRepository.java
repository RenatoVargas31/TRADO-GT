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
    //Proveedores con enabled = 1
    List<Proveedor> findAllByEnabled(Byte enabled);

    //Buscar proveedor por id con query method
    @Query("SELECT p FROM Proveedor p WHERE p.id = :id")
    Proveedor findByIdProveedor(Integer id);

    //Actualizar los atributos del proveedor teniedo en cuenta el id y el obetejo proveedor
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.telefonoProveedor = :telefonoProveedor, p.nombreTienda = :nombreTienda WHERE p.id = :id")
    void updateProveedor(String telefonoProveedor, String nombreTienda, Integer id);

    //Borrado lógico del proveedor teniendo en cuenta el id y el enabled = 0
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.enabled = 0 WHERE p.id = :id")
    void deleteProveedor(Integer id);
}
