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


    //<editor-fold desc="Buscar por isDeleted">
    List<Proveedor> findAllByIsDeleted(Byte isDeleted);
    //</editor-fold>

    //<editor-fold desc="Buscar por ID">
    @Query("SELECT p FROM Proveedor p WHERE p.id = :id")
    Proveedor buscarProveedorPorID(Integer id);
    //</editor-fold>

    //<editor-fold desc="Actualizar Proveedor">
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.telefono = :telefono, p.tienda = :tienda WHERE p.id = :id")
    void updateProveedor(String telefono, String tienda, Integer id);
    //</editor-fold>

    //<editor-fold desc="Borrar Proveedor">
    @Modifying
    @Transactional
    @Query("UPDATE Proveedor p SET p.isDeleted = 1 WHERE p.id = :id")
    void deleteProveedor(Integer id);
    //</editor-fold>
    //</editor-fold>

    @Query(value = """
        SELECT prov.idProveedor AS idProveedor,\s
                    prov.Nombre AS Proveedor,\s
                    prov.Telefono AS Telefono,\s
                    prov.Tienda AS Tienda\s
                    FROM Proveedor prov\s
                    JOIN Producto p ON prov.idProveedor = p.Proveedor_idProveedor\s
                    JOIN ProductoEnZona pz ON p.idProducto = pz.Producto_idProducto\s
                    JOIN ProductoEnZonaEnOrden pzo ON pz.Producto_idProducto = pzo.ProductoEnZona_Producto_idProducto\s
                    AND pz.Zona_idZona = pzo.ProductoEnZona_Zona_idZona\s
                    JOIN Orden o ON pzo.Orden_idOrden = o.idOrden\s
                    WHERE o.idOrden = ?1;
    """, nativeQuery = true)
    List<Object[]> findProveedorByOrderId(Integer idOrden);

}
