package com.app.tradogt.repository;

import com.app.tradogt.dto.OrdenCompraAgtDto;
import com.app.tradogt.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    //Órdenes totales
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
            orden o
        JOIN
            usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
            pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
            usuario a ON a.idUsuario = o.AgentCompra_idUsuario
        LEFT JOIN
            estadoordenagente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
        ORDER BY
            o.FechaCreacion DESC;
        
                                 
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNative();

    //Órdenes sin asignar
    @Query(value = """
        SELECT
            CONCAT(u.Nombre, ' ', u.Apellido) AS usuarioPropietario,
            u.idUsuario AS idUsuarioPropietario,
            DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.Metodo AS metodoPago,
            'No asignado' AS agenteCompra,
            NULL AS idAgenteCompra,
            eo.Nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            orden o
        JOIN
            usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
            pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
            estadoordenagente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
        WHERE
            eo.Nombre = 'Sin asignar'  -- Filtra por estado 'Sin asignar'
        ORDER BY
            o.FechaCreacion DESC;
        
                             
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeSinAsignar();

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
            a.idUsuario AS idAgenteCompra,
            eo.Nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            orden o
        JOIN
            usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
            pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
            usuario a ON a.idUsuario = o.AgentCompra_idUsuario
        LEFT JOIN
            estadoordenagente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
        WHERE
            eo.Nombre = 'Pendiente'  -- Filtra por estado 'Pendiente'
        ORDER BY
            o.FechaCreacion DESC;
        
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativePendiente();

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
            a.idUsuario AS idAgenteCompra,
            eo.Nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            orden o
        JOIN
            usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
            pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
            usuario a ON a.idUsuario = o.AgentCompra_idUsuario
        LEFT JOIN
            estadoordenagente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
        WHERE
            eo.Nombre = 'En proceso'  -- Filtra por estado 'En proceso'
        ORDER BY
            o.FechaCreacion DESC;
        
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeEnProceso();

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
            a.idUsuario AS idAgenteCompra,
            eo.Nombre AS estadoPedido,
            o.idOrden AS idOrden
        FROM
            orden o
        JOIN
            usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
            pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
            usuario a ON a.idUsuario = o.AgentCompra_idUsuario
        LEFT JOIN
            estadoordenagente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
        WHERE
            eo.Nombre = 'Resuelto'  -- Filtra por estado 'Resuelto'
        ORDER BY
            o.FechaCreacion DESC;
        
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeResuelto();



}
