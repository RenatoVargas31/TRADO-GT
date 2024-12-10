package com.app.tradogt.repository;

import com.app.tradogt.dto.ProductoMasVendidoDto;
import com.app.tradogt.entity.Categoria;
import com.app.tradogt.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

            SELECT
                p.idProducto AS idProducto,
                p.nombre AS Producto,
                p.precio AS Precio,
                pc.cantidad AS Cantidad,  -- Ajustado para reflejar la cantidad en ProductoEnCarrito
                pz.costoEnvio AS CostoEnvio
            FROM
                Producto p
            JOIN
                ProductoEnZona pz ON p.idProducto = pz.producto_idProducto  -- Relación con ProductoEnZona
            JOIN
                ProductoEnCarrito pc ON pz.producto_idProducto = pc.ProductoEnZona_producto_idProducto
                                      AND pz.zona_idZona = pc.ProductoEnZona_zona_idZona  -- Relación con ProductoEnCarrito
            JOIN
                Carrito c ON pc.Carrito_idCarrito = c.idCarrito  -- Relación con Carrito
            JOIN
                Orden o ON c.idCarrito = o.Carrito_idCarrito  -- Relación con la Orden
            WHERE
                o.idOrden = ?1;  -- Parámetro para filtrar por la orden específica
            -- Filtro dinámico por el ID de la orden
    """, nativeQuery = true)
    List<Object[]> findProductDetailsByOrderId(Integer idOrden);


    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +  // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +        // Suponiendo que hay una relación mapeada
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " + // Realizamos el JOIN con ProductoEnZona
            "WHERE c.id = 1 AND d.zonaIdzona.id = :zona AND d.cantidad > 25 order by  d.contar desc") // Filtrar por categoría, zona y cantidad
    List<Producto> findProductRopaMujer(@Param("zona") int zona);


    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +  // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +        // Suponiendo que hay una relación mapeada
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " + // Realizamos el JOIN con ProductoEnZona
            "WHERE c.id = 2 AND d.zonaIdzona.id = :zona AND d.cantidad > 25 order by  d.contar desc") // Filtrar por categoría, zona y cantidad
    List<Producto> findProductRopaHombre(@Param("zona") int zona);

    @Query("SELECT DISTINCT p.material FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo")
    List<String> findDistinctMaterials(@Param("tipo") int tipo);

    @Query("SELECT DISTINCT p.ram FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo AND p.ram IS NOT NULL " +
            "ORDER BY p.ram ASC")
    List<String> findDistinctRam(@Param("tipo") int tipo);


    @Query("SELECT DISTINCT p.almacenamiento FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo AND p.almacenamiento IS NOT NULL " +
            "ORDER BY p.ram ASC")
    List<String> findDistinctAlmacenamiento(@Param("tipo") int tipo);

    @Query("SELECT DISTINCT p.marca FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo")
    List<String> findDistinctMarca(@Param("tipo") int tipo);
    @Query("SELECT DISTINCT p.talla FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo")
    List<String> findDistinctTallas(@Param("tipo") int tipo);
    @Query("SELECT DISTINCT p.color FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo")
    List<String> findDistinctColores(@Param("tipo") int tipo);



    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +  // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +        // Suponiendo que hay una relación mapeada
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " + // Realizamos el JOIN con ProductoEnZona
            "WHERE c.id = 3 AND d.zonaIdzona.id = :zona AND d.cantidad > 25 order by  d.contar desc") // Filtrar por categoría, zona y cantidad
    List<Producto> findProductElectronico(@Param("zona") int zona);



    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +  // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +        // Suponiendo que hay una relación mapeada
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " + // Realizamos el JOIN con ProductoEnZona
            "WHERE c.id = 4 AND d.zonaIdzona.id = :zona AND d.cantidad > 25 order by  d.contar desc") // Filtrar por categoría, zona y cantidad
    List<Producto> findProductMuebles(@Param("zona") int zona);

    @Query("SELECT p FROM Producto p " +
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " +
            "WHERE d.zonaIdzona.id = :zona AND d.cantidad > 25 " +  // Filtrar por zona y cantidad
            "AND p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = 4) " +  // Solo productos que pertenecen a la categoría 4
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:material IS NULL OR LOWER(p.material) IN :material) " +
            "AND (:marca IS NULL OR LOWER(p.marca) IN :marca) " +
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductMueblesFilter(
            @Param("zona") int zona, // Asegúrate de incluir el parámetro de zona
            @Param("categoria") List<Integer> categoria,
            @Param("material") List<String> material,
            @Param("marca") List<String> marca,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);


    @Query("SELECT p FROM Producto p " +
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " +
            "JOIN p.subcategoriaIdsubcategoria s " + // Cambia p2 a p
            "JOIN s.categoriaIdcategoria c " +
            "WHERE d.zonaIdzona.id = :zona AND d.cantidad > 25 " +
            "AND c.id = 1 " +  // Solo productos que pertenecen a la categoría Mujer
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:talla IS NULL OR LOWER(p.talla) IN :talla) " +
            "AND (:marca IS NULL OR LOWER(p.marca) IN :marca) " +
            "AND (:color IS NULL OR LOWER(p.color) IN :color) " +
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductMujerFilter(
            @Param("zona") int zona, // Agrega el parámetro de zona
            @Param("categoria") List<Integer> categoria,
            @Param("talla") List<String> talla,
            @Param("marca") List<String> marca,
            @Param("color") List<String> color,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);

    @Query("SELECT p FROM Producto p " +
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " +
            "WHERE d.zonaIdzona.id = :zona AND d.cantidad > 25 " +  // Filtrar por el parámetro :zona y cantidad mayor a 25
            "AND p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = 2) " +  // Cambiar 2 a la categoría deseada si es necesario
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:talla IS NULL OR LOWER(p.talla) IN :talla) " +
            "AND (:marca IS NULL OR LOWER(p.marca) IN :marca) " +
            "AND (:color IS NULL OR LOWER(p.color) IN :color) " +
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductHombresFilter(
            @Param("zona") int zona,
            @Param("categoria") List<Integer> categoria,
            @Param("talla") List<String> talla,
            @Param("marca") List<String> marca,
            @Param("color") List<String> color,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);



    @Query("SELECT p FROM Producto p " +
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " +
            "WHERE d.zonaIdzona.id = :zona AND d.cantidad > 25 " +
            "AND p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = 3) " +  // Solo productos que pertenecen a la categoría 3
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:almacenamiento IS NULL OR LOWER(p.almacenamiento) IN :almacenamiento) " +
            "AND (:ram IS NULL OR LOWER(p.ram) IN :ram) " +
            "AND (:marca IS NULL OR LOWER(p.marca) IN :marca) " +
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductElectroFilter(
            @Param("zona") int zona,  // Agrega el parámetro de zona
            @Param("categoria") List<Integer> categoria,
            @Param("almacenamiento") List<String> almacenamiento,
            @Param("ram") List<String> ram,
            @Param("marca") List<String> marca,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);



    @Query("SELECT p FROM Producto p " +
            "JOIN ProductoEnZona d ON p.id = d.productoIdproducto.id " + // Realizamos el JOIN con ProductoEnZona
            "WHERE (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.proveedorIdproveedor.nombre) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND d.zonaIdzona.id = :zona " + // Filtrar por zonaId
            "AND p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo)")
    List<Producto> findProductQuery(@Param("zona") int zona,
                                    @Param("query") String query,
                                    @Param("tipo") int tipo
                                    );


    List<Producto> findAllByIsDeleted(Byte isDeleted);


    @Query("SELECT p FROM Producto p "
            + "JOIN ProductoEnCarrito pec ON p.id = pec.productoEnZona.productoIdproducto.id "
            + "JOIN Carrito c ON pec.carritoIdcarrito.id = c.id "
            + "JOIN Orden o ON o.carritoIdcarrito.id = c.id "
            + "WHERE o.estadoordenIdestadoorden.id = 7 AND c.usuarioIdusuario.id = :usuarioId")
    List<Producto> findProductosRecibidos(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT new com.app.tradogt.dto.ProductoMasVendidoDto(p.nombre, SUM(pc.cantidad)) FROM Producto p "
            + "JOIN ProductoEnCarrito pc ON p.id= pc.productoEnZona.productoIdproducto.id "
            + "JOIN Orden o ON pc.carritoIdcarrito.id = o.carritoIdcarrito.id "
            + "GROUP BY p.nombre "
            + "ORDER BY SUM(pc.cantidad) DESC "
            + "LIMIT 5")
    List<ProductoMasVendidoDto> getProductosMasVendidos();



}
