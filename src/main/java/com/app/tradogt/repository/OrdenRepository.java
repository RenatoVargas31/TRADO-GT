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
            u.Nombre AS usuarioPropietario,
            DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS fechaCreacion,
            p.Metodo AS metodoPago,
            CASE 
                WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
                ELSE a.Nombre
            END AS agenteCompra,
            eo.Nombre AS estadoPedido
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
            o.FechaCreacion DESC
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNative();

    //Órdenes sin asignar
    @Query(value = """
        SELECT
        u.Nombre AS UsuarioPropietario,
        DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS FechaCreacion,
        p.Metodo AS MetodoPago,
        CASE
        WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
        ELSE (SELECT a.Nombre FROM Usuario a WHERE a.idUsuario = o.AgentCompra_idUsuario)
        END AS AgenteCompra,
        eo.Nombre AS EstadoPedido
                FROM
        Orden o
        JOIN
        Usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
        Pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
        EstadoOrdenAgente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
                WHERE
        eo.Nombre = 'Sin asignar'  -- Filtra por estado 'Sin asignar'
        ORDER BY
        o.FechaCreacion DESC;
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeSinAsignar();

    //Órdenes pendientes
    @Query(value = """
        SELECT
        u.Nombre AS UsuarioPropietario,
        DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS FechaCreacion,
        p.Metodo AS MetodoPago,
        CASE
        WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
        ELSE (SELECT a.Nombre FROM Usuario a WHERE a.idUsuario = o.AgentCompra_idUsuario)
        END AS AgenteCompra,
        eo.Nombre AS EstadoPedido
                FROM
        Orden o
        JOIN
        Usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
        Pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
        EstadoOrdenAgente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
                WHERE
        eo.Nombre = 'Pendiente'  -- Filtra por estado 'Pendiente'
        ORDER BY
        o.FechaCreacion DESC;
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativePendiente();

    //Órdenes en proceso
    @Query(value = """
        SELECT
        u.Nombre AS UsuarioPropietario,
        DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS FechaCreacion,
        p.Metodo AS MetodoPago,
        CASE
        WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
        ELSE (SELECT a.Nombre FROM Usuario a WHERE a.idUsuario = o.AgentCompra_idUsuario)
        END AS AgenteCompra,
        eo.Nombre AS EstadoPedido
                FROM
        Orden o
        JOIN
        Usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
        Pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
        EstadoOrdenAgente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
                WHERE
        eo.Nombre = 'En proceso'  -- Filtra por estado 'En proceso'
        ORDER BY
        o.FechaCreacion DESC;
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeEnProceso();

    //Órdenes en resueltas
    @Query(value = """
        SELECT
        u.Nombre AS UsuarioPropietario,
        DATE_FORMAT(o.FechaCreacion, '%d-%m-%Y') AS FechaCreacion,
        p.Metodo AS MetodoPago,
        CASE
        WHEN o.AgentCompra_idUsuario IS NULL THEN 'No asignado'
        ELSE (SELECT a.Nombre FROM Usuario a WHERE a.idUsuario = o.AgentCompra_idUsuario)
        END AS AgenteCompra,
        eo.Nombre AS EstadoPedido
                FROM
        Orden o
        JOIN
        Usuario u ON o.Usuario_idUsuario = u.idUsuario
        LEFT JOIN
        Pago p ON p.Orden_idOrden = o.idOrden
        LEFT JOIN
        EstadoOrdenAgente eo ON eo.idEstadoOrdenAgente = o.EstadoOrdenAgente_idEstadoOrdenAgente
                WHERE
        eo.Nombre = 'Resuelto'  -- Filtra por estado 'Resuelto'
        ORDER BY
        o.FechaCreacion DESC;
    """, nativeQuery = true)
    List<Object[]> getOrderDetailsAsDtoNativeResuelto();



}
