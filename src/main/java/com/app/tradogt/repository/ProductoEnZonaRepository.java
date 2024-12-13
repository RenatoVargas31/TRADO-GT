package com.app.tradogt.repository;

import com.app.tradogt.dto.ImportacionesporImportador;
import com.app.tradogt.dto.ProductoStock;
import com.app.tradogt.entity.ProductoEnZona;
import com.app.tradogt.entity.ProductoEnZonaId;
import com.app.tradogt.entity.Zona;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoEnZonaRepository extends JpaRepository<ProductoEnZona, ProductoEnZonaId> {
    /*
    @Modifying
    @Query(value = "UPDATE productoEnZona SET estadoRepo = :nuevoEstado WHERE Producto_idProducto = :idProducto", nativeQuery = true)
    void actualizarEstadoOrden(@Param("nuevoEstado") Byte nuevoEstado, @Param("idProducto") Long idProducto);
    */
    List<ProductoEnZona> findAllByIsDeleted(Byte isDeleted);
    List<ProductoEnZona> findAllByZonaIdzonaAndIsDeleted(Zona zona, Byte isDeleted);
    //Para cuando se realiza el pago de las compras se debe reducir la cantidad en el stock local
   // List<ProductoEnZona> findByProductoIdproductoAndZonaIdzona(Producto producto, Zona zona);

    @Query("SELECT p FROM ProductoEnZona p WHERE p.productoIdproducto.id = :productoId AND p.zonaIdzona.id = :zonaId")
    Optional<ProductoEnZona> findByIdAndZona(@Param("productoId") int productoId, @Param("zonaId") int zona);

    @Query("SELECT p FROM ProductoEnZona p WHERE p.zonaIdzona.id = :idZona ORDER BY p.cantidad ASC")
    List<ProductoEnZona> findStockBajoZona(@Param("idZona") int idZona);


    @Query("SELECT p FROM ProductoEnZona p WHERE p.zonaIdzona.id = :idZona ORDER BY p.cantidad DESC")
    List<ProductoEnZona> findTop10Products(@Param("idZona") int idZona, Pageable pageable);


    @Query("SELECT new com.app.tradogt.dto.ImportacionesporImportador(prov.tienda, COUNT(pz.productoIdproducto.id), SUM(pz.cantidad)) "
            + "FROM ProductoEnZona pz "
            + "JOIN Producto p ON pz.productoIdproducto.id = p.id "
            + "JOIN Proveedor prov ON p.proveedorIdproveedor.id = prov.id "
            + "WHERE pz.zonaIdzona.id = :idZona "
            + "GROUP BY prov.tienda "
            + "ORDER BY SUM(pz.cantidad) DESC") // Ordenar por la suma de cantidades de mayor a menor
    List<ImportacionesporImportador> findTop10Importadores(@Param("idZona") int idZona, Pageable pageable);

    @Query("SELECT SUM(pz.cantidad) FROM ProductoEnZona pz WHERE pz.zonaIdzona.id = :idZona")
    int countStockTotal(@Param("idZona") int idZona);

    @Query("SELECT ROUND(CASE WHEN COUNT(pz) > 0 THEN SUM(pz.cantidad) * 1.0 / COUNT(pz) ELSE 0 END, 2) "
            + "FROM ProductoEnZona pz WHERE pz.zonaIdzona.id = :idZona")
    double stockPorProducto(@Param("idZona") int idZona);

    @Query("SELECT new com.app.tradogt.dto.ProductoStock(p.nombre, pz.cantidad) "
            + "FROM ProductoEnZona pz "
            + "JOIN pz.productoIdproducto p "
            + "WHERE pz.zonaIdzona.id = :idZona AND pz.cantidad = "
            + "(SELECT MIN(pz2.cantidad) FROM ProductoEnZona pz2 WHERE pz2.zonaIdzona.id = :idZona) "
            + "ORDER BY pz.id.productoIdproducto")
    List<ProductoStock> productStockMenor(@Param("idZona") int idZona);






    //Productos por zona con más calificados
    @Query(value = "SELECT \n" +
            "    p.producto_idProducto AS ID_Producto, \n" +
            "    pr.nombre AS Nombre_Producto, \n" +
            "    pr.marca AS Marca, \n" +
            "    pr.color AS Color, \n" +
            "    pr.foto AS Foto, \n" +
            "    pr.proveedor_idProveedor AS ID_Proveedor, \n" +
            "    pr.descripcion AS Descripcion, \n" +
            "    su.nombre AS Subcategoria, \n" +
            "    ca.nombre AS Categoria, \n" +
            "    p.zona_idZona AS ID_Zona, \n" +
            "    ROUND(AVG(r.calificacion), 1) AS Calificacion_Promedio, \n" +
            "    p.contar AS Cantidad, \n" +
            "    COUNT(r.Producto_idProducto) AS NumeroResena\n" +
            "FROM \n" +
            "    ProductoEnZona p\n" +
            "JOIN \n" +
            "    Resena r ON r.Producto_idProducto = p.producto_idProducto\n" +
            "JOIN \n" +
            "    Producto pr ON pr.idProducto = p.producto_idProducto\n" +
            "JOIN \n" +
            "    SubCategoria su ON su.idSubCategoria = pr.subCategoria_idSubCategoria\n" +
            "JOIN \n" +
            "    Categoria ca ON ca.idCategoria = su.Categoria_idCategoria\n" +
            "WHERE \n" +
            "    p.zona_idZona = :idZona\n" +
            "GROUP BY \n" +
            "    p.producto_idProducto, \n" +
            "    pr.nombre, \n" +
            "    pr.marca, \n" +
            "    pr.color, \n" +
            "    pr.foto, \n" +
            "    pr.proveedor_idProveedor, \n" +
            "    pr.descripcion, \n" +
            "    su.nombre, \n" +
            "    ca.nombre, \n" +
            "    p.zona_idZona, \n" +
            "    p.contar\n" +
            "ORDER BY \n" +
            "    Calificacion_Promedio DESC, \n" +
            "    p.contar DESC\n" +
            "LIMIT 5;",
            nativeQuery = true)
    List<Object[]> productosTop(@Param("idZona") int idZona);











}
