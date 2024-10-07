package com.app.tradogt.repository;

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


    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " + // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +    // Suponiendo que hay una relación mapeada
            "WHERE c.id = 1")
    List<Producto> findProductRopaMujer();

    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " + // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +    // Suponiendo que hay una relación mapeada
            "WHERE c.id = 2") List<Producto> findProductRopaHombre();

    @Query("SELECT DISTINCT p.material FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = :tipo")
    List<String> findDistinctMaterials(@Param("tipo") int tipo);



    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " + // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +    // Suponiendo que hay una relación mapeada
            "WHERE c.id = 3")
    List<Producto> findProductElectronico();



    @Query("SELECT p FROM Producto p " +
            "JOIN p.subcategoriaIdsubcategoria s " + // Suponiendo que hay una relación mapeada
            "JOIN s.categoriaIdcategoria c " +    // Suponiendo que hay una relación mapeada
            "WHERE c.id = 4")
    List<Producto> findProductMuebles();

    @Query("SELECT p FROM Producto p " +
            "WHERE p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = 4) " +  // Solo productos que pertenecen a la categoría 4
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:material IS NULL OR LOWER(p.material) IN :material)"+
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductMueblesFilter(
            @Param("categoria") List<Integer> categoria, // Cambia a List<Integer>
            @Param("material") List<String> material,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);

    @Query("SELECT p FROM Producto p " +
            "WHERE p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = 1) " +  // Solo productos que pertenecen a la categoría Mujer
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:material IS NULL OR LOWER(p.material) IN :material)"+
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductMujerFilter(
            @Param("categoria") List<Integer> categoria, // Cambia a List<Integer>
            @Param("material") List<String> material,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);

    @Query("SELECT p FROM Producto p " +
            "WHERE p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = 2) " +  // Solo productos que pertenecen a la categoría 4
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:material IS NULL OR LOWER(p.material) IN :material)"+
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductHombresFilter(
            @Param("categoria") List<Integer> categoria, // Cambia a List<Integer>
            @Param("material") List<String> material,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);

    @Query("SELECT p FROM Producto p " +
            "WHERE p IN (SELECT p2 FROM Producto p2 " +
            "JOIN p2.subcategoriaIdsubcategoria s " +
            "JOIN s.categoriaIdcategoria c " +
            "WHERE c.id = 3) " +  // Solo productos que pertenecen a la categoría 4
            "AND (:categoria IS NULL OR p.subcategoriaIdsubcategoria.id IN :categoria) " +
            "AND (:material IS NULL OR LOWER(p.material) IN :material)"+
            "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
            "AND (:precioMax IS NULL OR p.precio <= :precioMax)")
    List<Producto> findProductElectroFilter(
            @Param("categoria") List<Integer> categoria, // Cambia a List<Integer>
            @Param("material") List<String> material,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax);

}
