package com.app.tradogt.repository;

import com.app.tradogt.dto.OrdenCompraAgtDto;
import com.app.tradogt.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {
    /*
    //Órdenes totales (no eliminadas)
    @Query(value = """
        SELECT
                                           CONCAT(u.Nombre, ' ', u.Apellido) AS usuarioPropietario,
                                           u.idUsuario AS idUsuarioPropietario,
                                           DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
                                           p.Metodo AS metodoPago,
                                           CASE
                                               WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
                                               ELSE a.Nombre
                                           END AS agenteCompra,
                                           COALESCE(a.idUsuario, 0) AS idAgenteCompra,  -- Aquí usamos COALESCE para devolver 0 si es NULL
                                           eo.Nombre AS estadoPedido,
                                           o.idOrden AS idOrden
                                       FROM
                                           Orden o
                                       JOIN
                                           Usuario u ON o.Usuario_idUsuario = u.idUsuario
                                       LEFT JOIN
                                        Pago p ON p.Orden_idOrden = o.idOrden
                                       LEFT JOIN
                                           Usuario a ON a.idUsuario = o.AgentCompra_idUsuario
                                       LEFT JOIN
                                           EstadoOrden eo ON eo.idEstadoOrden = o.EstadoOrden_idEstadoOrden
                                       WHERE
                                           o.isDeleted = 0  -- Solo mostrar órdenes que no han sido eliminadas
                                           and a.idUsuario = ?1
                                       ORDER BY
                                           o.FechaCreacion DESC;
        
                                 
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNative(Integer idAgente);

    //Órdenes sin asignar
    @Query(value = """
    SELECT
                                           CONCAT(u.Nombre, ' ', u.Apellido) AS usuarioPropietario,
                                           u.idUsuario AS idUsuarioPropietario,
                                           DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
                                           p.Metodo AS metodoPago,
                                           CASE
                                               WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
                                               ELSE a.Nombre
                                           END AS agenteCompra,
                                           COALESCE(a.idUsuario, 0) AS idAgenteCompra,  -- Aquí usamos COALESCE para devolver 0 si es NULL
                                           eo.Nombre AS estadoPedido,
                                           o.idOrden AS idOrden
                                       FROM
                                           Orden o
                                       JOIN
                                           Usuario u ON o.Usuario_idUsuario = u.idUsuario
                                       LEFT JOIN
                                        Pago p ON p.Orden_idOrden = o.idOrden
                                       LEFT JOIN
                                           Usuario a ON a.idUsuario = o.AgentCompra_idUsuario
                                       LEFT JOIN
                                           EstadoOrden eo ON eo.idEstadoOrden = o.EstadoOrden_idEstadoOrden
                                       WHERE
                                           eo.Nombre = 'CREADO' AND o.isDeleted = 0  -- Solo mostrar órdenes que no han sido eliminadas
                                           and a.idUsuario = ?1
                                       ORDER BY
                                           o.FechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeSinAsignar(Integer idAgente);


    //Órdenes pendientes
    @Query(value = """
    SELECT
                                           CONCAT(u.Nombre, ' ', u.Apellido) AS usuarioPropietario,
                                           u.idUsuario AS idUsuarioPropietario,
                                           DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
                                           p.Metodo AS metodoPago,
                                           CASE
                                               WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
                                               ELSE a.Nombre
                                           END AS agenteCompra,
                                           COALESCE(a.idUsuario, 0) AS idAgenteCompra,  -- Aquí usamos COALESCE para devolver 0 si es NULL
                                           eo.Nombre AS estadoPedido,
                                           o.idOrden AS idOrden
                                       FROM
                                           Orden o
                                       JOIN
                                           Usuario u ON o.Usuario_idUsuario = u.idUsuario
                                       LEFT JOIN
                                        Pago p ON p.Orden_idOrden = o.idOrden
                                       LEFT JOIN
                                           Usuario a ON a.idUsuario = o.AgentCompra_idUsuario
                                       LEFT JOIN
                                           EstadoOrden eo ON eo.idEstadoOrden = o.EstadoOrden_idEstadoOrden
                                       WHERE
                                           eo.Nombre = 'EN VALIDACION' AND o.isDeleted = 0  -- Solo mostrar órdenes que no han sido eliminadas
                                           and a.idUsuario = ?1
                                       ORDER BY
                                           o.FechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativePendiente(Integer idAgente);


    //Órdenes en proceso
    @Query(value = """
    SELECT
                                           CONCAT(u.Nombre, ' ', u.Apellido) AS usuarioPropietario,
                                           u.idUsuario AS idUsuarioPropietario,
                                           DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
                                           p.Metodo AS metodoPago,
                                           CASE
                                               WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
                                               ELSE a.Nombre
                                           END AS agenteCompra,
                                           COALESCE(a.idUsuario, 0) AS idAgenteCompra,  -- Aquí usamos COALESCE para devolver 0 si es NULL
                                           eo.Nombre AS estadoPedido,
                                           o.idOrden AS idOrden
                                       FROM
                                           Orden o
                                       JOIN
                                           Usuario u ON o.Usuario_idUsuario = u.idUsuario
                                       LEFT JOIN
                                        Pago p ON p.Orden_idOrden = o.idOrden
                                       LEFT JOIN
                                           Usuario a ON a.idUsuario = o.AgentCompra_idUsuario
                                       LEFT JOIN
                                           EstadoOrden eo ON eo.idEstadoOrden = o.EstadoOrden_idEstadoOrden
                                       WHERE
                                           eo.Nombre IN ('EN PROCESO','ARRIBO AL PAIS','EN ADUANAS','EN RUTA') AND o.isDeleted = 0  -- Solo mostrar órdenes que no han sido eliminadas
                                           and a.idUsuario = ?1
                                       ORDER BY
                                           o.FechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeEnProceso(Integer idAgente);


    //Órdenes en resueltas
    @Query(value = """
    SELECT
                                           CONCAT(u.Nombre, ' ', u.Apellido) AS usuarioPropietario,
                                           u.idUsuario AS idUsuarioPropietario,
                                           DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
                                           p.Metodo AS metodoPago,
                                           CASE
                                               WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
                                               ELSE a.Nombre
                                           END AS agenteCompra,
                                           COALESCE(a.idUsuario, 0) AS idAgenteCompra,  -- Aquí usamos COALESCE para devolver 0 si es NULL
                                           eo.Nombre AS estadoPedido,
                                           o.idOrden AS idOrden
                                       FROM
                                           Orden o
                                       JOIN
                                           Usuario u ON o.Usuario_idUsuario = u.idUsuario
                                       LEFT JOIN
                                        Pago p ON p.Orden_idOrden = o.idOrden
                                       LEFT JOIN
                                           Usuario a ON a.idUsuario = o.AgentCompra_idUsuario
                                       LEFT JOIN
                                           EstadoOrden eo ON eo.idEstadoOrden = o.EstadoOrden_idEstadoOrden
                                       WHERE
                                           eo.Nombre = 'RECIBIDO' AND o.isDeleted = 0  -- Solo mostrar órdenes que no han sido eliminadas
                                           and a.idUsuario = ?1
                                       ORDER BY
                                           o.FechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeResuelto(Integer idAgente);

    //Órdenes por usuario (órdenes de un solo usuario)
    @Query(value = """
        SELECT
                                           CONCAT(u.Nombre, ' ', u.Apellido) AS usuarioPropietario,
                                           u.idUsuario AS idUsuarioPropietario,
                                           DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
                                           p.Metodo AS metodoPago,
                                           CASE
                                               WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
                                               ELSE a.Nombre
                                           END AS agenteCompra,
                                           COALESCE(a.idUsuario, 0) AS idAgenteCompra,  -- Aquí usamos COALESCE para devolver 0 si es NULL
                                           eo.Nombre AS estadoPedido,
                                           o.idOrden AS idOrden
                                       FROM
                                           Orden o
                                       JOIN
                                           Usuario u ON o.Usuario_idUsuario = u.idUsuario
                                       LEFT JOIN
                                        Pago p ON p.Orden_idOrden = o.idOrden
                                       LEFT JOIN
                                           Usuario a ON a.idUsuario = o.AgentCompra_idUsuario
                                       LEFT JOIN
                                           EstadoOrden eo ON eo.idEstadoOrden = o.EstadoOrden_idEstadoOrden
                                       WHERE
                                           o.isDeleted = 0  -- Solo mostrar órdenes que no han sido eliminadas
                                           and a.idUsuario = ?1
                                           and u.idUsuario = ?2
                                       ORDER BY
                                           o.FechaCreacion DESC;
        
                                 
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeParUsuario(Integer idAgente, Integer idUsuario);


    //Lista de orden por fecha de creación y fecha de abordo
    @Query(value = "SELECT o.idOrden AS id,\n" +
            "       p.Nombre AS nombre_producto,\n" +
            "       c.Nombre AS categoria,\n" +
            "       DATE_FORMAT(o.FechaArribo, '%d/%m/%y') AS fecha_arribo,\n" +
            "       DATE_FORMAT(o.FechaCreacion, '%d/%m/%y') AS fecha_creacion\n" +
            "FROM `TRADO_DB`.`Orden` o\n" +
            "JOIN `TRADO_DB`.`ProductoEnZonaEnOrden` pzo\n" +
            "    ON o.idOrden = pzo.Orden_idOrden\n" +
            "JOIN `TRADO_DB`.`ProductoEnZona` pz\n" +
            "    ON pzo.ProductoEnZona_Producto_idProducto = pz.Producto_idProducto\n" +
            "JOIN `TRADO_DB`.`Producto` p\n" +
            "    ON pz.Producto_idProducto = p.idProducto\n" +
            "JOIN `TRADO_DB`.`SubCategoria` sc\n" +
            "    ON p.SubCategoria_idSubCategoria = sc.idSubCategoria\n" +
            "JOIN `TRADO_DB`.`Categoria` c\n" +
            "    ON sc.Categoria_idCategoria = c.idCategoria\n" +
            "WHERE pz.Zona_idZona = 1;", nativeQuery = true)

    List<Object[]> findOrdersByZone();


    //Listar ordenes de un usuario
    List<Orden> findAllByEsCarritoAndIsDeleted(int i, int k);

    Optional<Orden> findByCodigo(String codigo);


*/

}
