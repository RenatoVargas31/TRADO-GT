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
        SELECT
            prov.idProveedor AS idProveedor,
            prov.nombre AS Proveedor,
            prov.telefono AS Telefono,
            prov.tienda AS Tienda
        FROM
            Proveedor prov
        JOIN
            Producto p ON prov.idProveedor = p.proveedor_idProveedor  -- Relación con la tabla Producto
        JOIN
            ProductoEnZona pz ON p.idProducto = pz.producto_idProducto  -- Relación con ProductoEnZona
        JOIN
            ProductoEnCarrito pc ON pz.producto_idProducto = pc.ProductoEnZona_producto_idProducto\s
                                  AND pz.zona_idZona = pc.ProductoEnZona_zona_idZona  -- Relación con ProductoEnCarrito
        JOIN
            Carrito c ON pc.Carrito_idCarrito = c.idCarrito  -- Relación con Carrito
        JOIN
            Orden o ON c.idCarrito = o.Carrito_idCarrito  -- Relación con la Orden
        WHERE
            o.idOrden = ?1;  -- Parámetro para filtrar por la orden específica
        -- Filtro por el ID de la orden
    """, nativeQuery = true)
    List<Object[]> findProveedorByOrderId(Integer idOrden);
    @Query("SELECT p FROM Proveedor p WHERE p.isDeleted = 1")
    List<Proveedor> getProveedorBaneado();

}
