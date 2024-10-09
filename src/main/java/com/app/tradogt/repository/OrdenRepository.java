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

    //Órdenes totales (no eliminadas)
    @Query(value = """
        SELECT
            CONCAT(u.nombre, ' ', u.apellido) AS usuarioPropietario,
            u.idUsuario AS idUsuarioPropietario,
            DATE_FORMAT(o.fechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.metodo AS metodoPago,
            CASE
                WHEN o.agentCompra_idUsuario IS NULL THEN 'No asignado'
                ELSE a.nombre
            END AS agenteCompra,
            COALESCE(a.idUsuario, 0) AS idAgenteCompra,
            eo.nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            Orden o
        JOIN
            Carrito c ON o.Carrito_idCarrito = c.idCarrito  -- Actualizado para relacionar Orden con Carrito
        JOIN
            Usuario u ON c.Usuario_idUsuario = u.idUsuario  -- Relación con el usuario propietario
        LEFT JOIN
            Pago p ON p.idPago = o.Pago_idPago  -- Relación con la tabla de pagos
        LEFT JOIN
            Usuario a ON a.idUsuario = o.agentCompra_idUsuario  -- Relación con el agente de compra
        LEFT JOIN
            EstadoOrden eo ON eo.idEstadoOrden = o.estadoOrden_idEstadoOrden  -- Relación con el estado de la orden
        WHERE
            o.isDeleted = 0  -- Solo mostrar órdenes no eliminadas
            AND a.idUsuario = ?1  -- Filtro para el agente de compra específico
        GROUP BY
            o.idOrden  -- Agrupar por la orden para no repetir filas por producto
        ORDER BY
            o.fechaCreacion DESC;
        
        
                                 
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNative(Integer idAgente);

    //Órdenes sin asignar
    @Query(value = """
    SELECT
            CONCAT(u.nombre, ' ', u.apellido) AS usuarioPropietario,
            u.idUsuario AS idUsuarioPropietario,
            DATE_FORMAT(o.fechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.metodo AS metodoPago,
            CASE
                WHEN o.agentCompra_idUsuario IS NULL THEN 'No asignado'
                ELSE a.nombre
            END AS agenteCompra,
            COALESCE(a.idUsuario, 0) AS idAgenteCompra,
            eo.nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            Orden o
        JOIN
            Carrito c ON o.Carrito_idCarrito = c.idCarrito  -- Actualizado para relacionar Orden con Carrito
        JOIN
            Usuario u ON c.Usuario_idUsuario = u.idUsuario  -- Relación con el usuario propietario
        LEFT JOIN
            Pago p ON p.idPago = o.Pago_idPago  -- Relación con la tabla de pagos
        LEFT JOIN
            Usuario a ON a.idUsuario = o.agentCompra_idUsuario  -- Relación con el agente de compra
        LEFT JOIN
            EstadoOrden eo ON eo.idEstadoOrden = o.estadoOrden_idEstadoOrden  -- Relación con el estado de la orden
        WHERE
            eo.Nombre = 'CREADO' AND o.isDeleted = 0  -- Solo mostrar órdenes no eliminadas
        GROUP BY
            o.idOrden  -- Agrupar por la orden para no repetir filas por producto
        ORDER BY
            o.fechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeSinAsignar();


    //Órdenes pendientes
    @Query(value = """
    SELECT
            CONCAT(u.nombre, ' ', u.apellido) AS usuarioPropietario,
            u.idUsuario AS idUsuarioPropietario,
            DATE_FORMAT(o.fechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.metodo AS metodoPago,
            CASE
                WHEN o.agentCompra_idUsuario IS NULL THEN 'No asignado'
                ELSE a.nombre
            END AS agenteCompra,
            COALESCE(a.idUsuario, 0) AS idAgenteCompra,
            eo.nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            Orden o
        JOIN
            Carrito c ON o.Carrito_idCarrito = c.idCarrito  -- Actualizado para relacionar Orden con Carrito
        JOIN
            Usuario u ON c.Usuario_idUsuario = u.idUsuario  -- Relación con el usuario propietario
        LEFT JOIN
            Pago p ON p.idPago = o.Pago_idPago  -- Relación con la tabla de pagos
        LEFT JOIN
            Usuario a ON a.idUsuario = o.agentCompra_idUsuario  -- Relación con el agente de compra
        LEFT JOIN
            EstadoOrden eo ON eo.idEstadoOrden = o.estadoOrden_idEstadoOrden  -- Relación con el estado de la orden
        WHERE
            eo.Nombre = 'EN VALIDACION' AND o.isDeleted = 0  -- Solo mostrar órdenes no eliminadas
            AND a.idUsuario = ?1  -- Filtro para el agente de compra específico
        GROUP BY
            o.idOrden  -- Agrupar por la orden para no repetir filas por producto
        ORDER BY
            o.fechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativePendiente(Integer idAgente);


    //Órdenes en proceso
    @Query(value = """
    SELECT
            CONCAT(u.nombre, ' ', u.apellido) AS usuarioPropietario,
            u.idUsuario AS idUsuarioPropietario,
            DATE_FORMAT(o.fechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.metodo AS metodoPago,
            CASE
                WHEN o.agentCompra_idUsuario IS NULL THEN 'No asignado'
                ELSE a.nombre
            END AS agenteCompra,
            COALESCE(a.idUsuario, 0) AS idAgenteCompra,
            eo.nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            Orden o
        JOIN
            Carrito c ON o.Carrito_idCarrito = c.idCarrito  -- Actualizado para relacionar Orden con Carrito
        JOIN
            Usuario u ON c.Usuario_idUsuario = u.idUsuario  -- Relación con el usuario propietario
        LEFT JOIN
            Pago p ON p.idPago = o.Pago_idPago  -- Relación con la tabla de pagos
        LEFT JOIN
            Usuario a ON a.idUsuario = o.agentCompra_idUsuario  -- Relación con el agente de compra
        LEFT JOIN
            EstadoOrden eo ON eo.idEstadoOrden = o.estadoOrden_idEstadoOrden  -- Relación con el estado de la orden
        WHERE
            eo.Nombre IN ('EN PROCESO','ARRIBO AL PAIS','EN ADUANAS','EN RUTA') AND o.isDeleted = 0  -- Solo mostrar órdenes no eliminadas
            AND a.idUsuario = ?1  -- Filtro para el agente de compra específico
        GROUP BY
            o.idOrden  -- Agrupar por la orden para no repetir filas por producto
        ORDER BY
            o.fechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeEnProceso(Integer idAgente);


    //Órdenes en resueltas
    @Query(value = """
    SELECT
            CONCAT(u.nombre, ' ', u.apellido) AS usuarioPropietario,
            u.idUsuario AS idUsuarioPropietario,
            DATE_FORMAT(o.fechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.metodo AS metodoPago,
            CASE
                WHEN o.agentCompra_idUsuario IS NULL THEN 'No asignado'
                ELSE a.nombre
            END AS agenteCompra,
            COALESCE(a.idUsuario, 0) AS idAgenteCompra,
            eo.nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            Orden o
        JOIN
            Carrito c ON o.Carrito_idCarrito = c.idCarrito  -- Actualizado para relacionar Orden con Carrito
        JOIN
            Usuario u ON c.Usuario_idUsuario = u.idUsuario  -- Relación con el usuario propietario
        LEFT JOIN
            Pago p ON p.idPago = o.Pago_idPago  -- Relación con la tabla de pagos
        LEFT JOIN
            Usuario a ON a.idUsuario = o.agentCompra_idUsuario  -- Relación con el agente de compra
        LEFT JOIN
            EstadoOrden eo ON eo.idEstadoOrden = o.estadoOrden_idEstadoOrden  -- Relación con el estado de la orden
        WHERE
            eo.Nombre = 'RECIBIDO' AND o.isDeleted = 0  -- Solo mostrar órdenes no eliminadas
            AND a.idUsuario = ?1  -- Filtro para el agente de compra específico
        GROUP BY
            o.idOrden  -- Agrupar por la orden para no repetir filas por producto
        ORDER BY
            o.fechaCreacion DESC;
""", nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeResuelto(Integer idAgente);

    //Órdenes por usuario (órdenes de un solo usuario)
    @Query(value = """
        SELECT
            CONCAT(u.nombre, ' ', u.apellido) AS usuarioPropietario,
            u.idUsuario AS idUsuarioPropietario,
            DATE_FORMAT(o.fechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.metodo AS metodoPago,
            CASE
                WHEN o.agentCompra_idUsuario IS NULL THEN 'No asignado'
                ELSE a.nombre
            END AS agenteCompra,
            COALESCE(a.idUsuario, 0) AS idAgenteCompra,
            eo.nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            Orden o
        JOIN
            Carrito c ON o.Carrito_idCarrito = c.idCarrito  -- Relación con el carrito actualizada
        JOIN
            Usuario u ON c.Usuario_idUsuario = u.idUsuario  -- Relación con el usuario propietario a través del carrito
        LEFT JOIN
            Pago p ON p.idPago = o.Pago_idPago  -- Relación con la tabla de pagos
        LEFT JOIN
            Usuario a ON a.idUsuario = o.agentCompra_idUsuario  -- Relación con el agente de compra
        LEFT JOIN
            EstadoOrden eo ON eo.idEstadoOrden = o.estadoOrden_idEstadoOrden  -- Relación con el estado de la orden
        WHERE
            o.isDeleted = 0  -- Solo mostrar órdenes que no han sido eliminadas
            AND a.idUsuario = ?1  -- Parámetro para filtrar por el agente de compra
            AND u.idUsuario = ?2  -- Parámetro para filtrar por el usuario propietario
        GROUP BY
            o.idOrden  -- Agrupar por la orden para no duplicar filas
        ORDER BY
            o.fechaCreacion DESC;
        
                                 
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

    List<Orden> findAllByIsDeleted( int k);

    Optional<Orden> findByCodigo(String codigo);


}
