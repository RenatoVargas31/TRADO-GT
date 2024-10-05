package com.app.tradogt.repository;

import com.app.tradogt.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosRepository extends JpaRepository<Producto, Integer> {
/*
    @Query(value = "SELECT \n" +
            "    p.nombre AS nombre_producto,\n" +
            "    c.nombre AS categoria,\n" +
            "    sc.nombre AS subcategoria,\n" +
            "    pez.Cantidad,\n" +
            "    pez.estadoRepo AS estado,\n" +
            "    z.nombre AS zona\n" +
            "FROM TRADO_DB.ProductoEnZona pez\n" +
            "JOIN TRADO_DB.Producto p ON pez.Producto_idProducto = p.idProducto\n" +
            "JOIN TRADO_DB.Zona z ON pez.Zona_idZona = z.idZona\n" +
            "JOIN TRADO_DB.SubCategoria sc ON p.SubCategoria_idSubCategoria = sc.idSubCategoria\n" +
            "JOIN TRADO_DB.Categoria c ON sc.Categoria_idCategoria = c.idCategoria\n" +
            "WHERE pez.Zona_idZona = 1\n" +
            "ORDER BY p.nombre;", nativeQuery = true)
    List<Object[]> findOrdersByRepo();


    @Query(value = "SELECT p.idProducto AS idProducto, " +  // Agregamos el idProducto aquí
            "p.Nombre AS Producto, " +
            "p.Precio AS Precio, " +
            "pzo.Cantidad AS Cantidad, " +
            "pz.CostoEnvio AS CostoEnvio " +
            "FROM producto p " +
            "JOIN productoenzona pz ON p.idProducto = pz.Producto_idProducto " +
            "JOIN productoenzonaenorden pzo ON pz.Producto_idProducto = pzo.ProductoEnZona_Producto_idProducto " +
            "AND pz.Zona_idZona = pzo.ProductoEnZona_Zona_idZona " +
            "JOIN orden o ON pzo.Orden_idOrden = o.idOrden " +
            "WHERE o.idOrden = :idOrden", nativeQuery = true)
    List<Object[]> findProductDetailsByOrderId(Integer idOrden);
*/
}
