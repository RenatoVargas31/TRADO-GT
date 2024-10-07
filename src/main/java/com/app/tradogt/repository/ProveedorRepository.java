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
        SELECT\s
            prov.idProveedor AS idProveedor,
            prov.nombre AS Proveedor,
            prov.telefono AS Telefono,
            prov.tienda AS Tienda
        FROM\s
            Proveedor prov
        JOIN\s
            Producto p ON prov.idProveedor = p.Proveedor_idProveedor  -- Relación correcta con la tabla Producto
        JOIN\s
            ProductoEnZona pz ON p.idProducto = pz.producto_idProducto  -- Relación con ProductoEnZona
        JOIN\s
            Carrito c ON pz.producto_idProducto = c.ProductoEnZona_producto_idProducto\s
                       AND pz.zona_idZona = c.ProductoEnZona_zona_idZona  -- Relación con Carrito
        JOIN\s
            Orden o ON c.Orden_idOrden = o.idOrden  -- Relación con la Orden
        WHERE\s
            o.idOrden = ?1;  -- Filtro por el ID de la orden
    """, nativeQuery = true)
    List<Object[]> findProveedorByOrderId(Integer idOrden);

}
