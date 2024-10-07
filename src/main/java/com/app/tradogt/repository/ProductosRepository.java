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

            SELECT\s
                p.idProducto AS idProducto,
                p.nombre AS Producto,
                p.precio AS Precio,
                c.cantidad AS Cantidad,
                pz.costoEnvio AS CostoEnvio
            FROM\s
                Producto p
            JOIN\s
                ProductoEnZona pz ON p.idProducto = pz.producto_idProducto  -- Relación con ProductoEnZona
            JOIN\s
                Carrito c ON pz.producto_idProducto = c.ProductoEnZona_producto_idProducto\s
                          AND pz.zona_idZona = c.ProductoEnZona_zona_idZona  -- Relación con Carrito
            JOIN\s
                Orden o ON c.Orden_idOrden = o.idOrden  -- Relación con la Orden
            WHERE\s
                o.idOrden = ?1;  -- Filtro dinámico por el ID de la orden
    """, nativeQuery = true)
    List<Object[]> findProductDetailsByOrderId(Integer idOrden);

}
