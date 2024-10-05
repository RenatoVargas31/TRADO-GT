package com.app.tradogt.repository;

import com.app.tradogt.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosRepository extends JpaRepository<Producto, Integer> {

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


    @Query(value = """

            SELECT p.idProducto AS idProducto,\s
                p.Nombre AS Producto,\s
                p.Precio AS Precio,\s
                pzo.Cantidad AS Cantidad,\s
                pz.CostoEnvio AS CostoEnvio\s
                FROM Producto p\s
                JOIN ProductoEnZona pz ON p.idProducto = pz.Producto_idProducto\s
                JOIN ProductoEnZonaEnOrden pzo ON pz.Producto_idProducto = pzo.ProductoEnZona_Producto_idProducto\s
                AND pz.Zona_idZona = pzo.ProductoEnZona_Zona_idZona\s
                JOIN Orden o ON pzo.Orden_idOrden = o.idOrden\s
                WHERE o.idOrden = ?1;
    """, nativeQuery = true)
    List<Object[]> findProductDetailsByOrderId(Integer idOrden);

}
