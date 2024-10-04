package com.app.tradogt.repository;

import com.app.tradogt.dto.ProveedorInfoAgtDto;
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
    List<Proveedor> findAllByIsDeleted(Byte isDeleted);
    //</editor-fold>

    //<editor-fold desc="Buscar por ID">
    @Query("SELECT p FROM Proveedor p WHERE p.id = :id")
    Proveedor findByIdProveedor(Integer id);
    //</editor-fold>

    //<editor-fold desc="Actualizar Proveedor">
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.telefono = :telefonoProveedor, p.tienda = :nombreTienda WHERE p.id = :id")
    void updateProveedor(String telefonoProveedor, String nombreTienda, Integer id);
    //</editor-fold>

    //<editor-fold desc="Borrar Proveedor">
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.isDeleted = 1 WHERE p.id = :id")
    void deleteProveedor(Integer id);
    //</editor-fold>
    //</editor-fold>

    @Query(value = "SELECT prov.idProveedor AS idProveedor, " +
            "prov.Nombre AS Proveedor, " +
            "prov.Telefono AS Telefono, " +
            "prov.Tienda AS Tienda " +
            "FROM proveedor prov " +
            "JOIN producto p ON prov.idProveedor = p.Proveedor_idProveedor " +
            "JOIN productoenzona pz ON p.idProducto = pz.Producto_idProducto " +
            "JOIN productoenzonaenorden pzo ON pz.Producto_idProducto = pzo.ProductoEnZona_Producto_idProducto " +
            "AND pz.Zona_idZona = pzo.ProductoEnZona_Zona_idZona " +
            "JOIN orden o ON pzo.Orden_idOrden = o.idOrden " +
            "WHERE o.idOrden = :idOrden", nativeQuery = true)
    List<Object[]> findProveedorByOrderId(Integer idOrden);

}
