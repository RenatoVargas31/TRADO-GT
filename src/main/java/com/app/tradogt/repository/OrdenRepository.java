package com.app.tradogt.repository;

import com.app.tradogt.dto.OrdenCompraAgtDto;
import com.app.tradogt.dto.OrdenEstadoDto;
import com.app.tradogt.entity.EstadoOrden;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    @Query(value = """
            SELECT o.codigo,
                   p.nombre AS nombreProducto,
                   c.costoTotal,
                   pec.cantidad,
                   o.fechaCreacion,
                   eo.nombre AS estadoOrden,
                   (SELECT CONCAT(ag.nombre, ' ', ag.apellido)
                    FROM Usuario AS ag
                    WHERE ag.idUsuario = o.agentCompra_idUsuario) AS nombreAgenteCompra
            FROM Usuario AS u
            INNER JOIN Carrito AS c ON u.idUsuario = c.Usuario_idUsuario
            INNER JOIN Orden AS o ON o.Carrito_idCarrito = c.idCarrito
            INNER JOIN EstadoOrden AS eo ON eo.idEstadoOrden = o.estadoOrden_idEstadoOrden
            INNER JOIN ProductoEnCarrito AS pec ON pec.Carrito_idCarrito = c.idCarrito
            INNER JOIN Producto AS p ON p.idProducto = pec.ProductoEnZona_producto_idProducto
            WHERE u.idUsuario = :userId 
                AND eo.idEstadoOrden != 7
            GROUP BY u.nombre, u.apellido, p.nombre, o.idOrden
            """, nativeQuery = true)
    List<Object[]> obtenerPedidosPorUsuario(@Param("userId") int userId);





    //Órdenes totales (no eliminadas)
    @Query(value = """
        SELECT
                    CONCAT(u.nombre, ' ', u.apellido) AS usuarioPropietario,
                    u.idUsuario AS idUsuarioPropietario,
                    DATE_FORMAT(o.fechaCreacion, '%d-%m-%Y') AS fechaCreacion,
                    p.monto AS montoTotal,
                    o.codigo AS codigoOrden,
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
                    p.monto AS montoTotal,
                    o.codigo AS codigoOrden,
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
                    p.monto AS montoTotal,
                    o.codigo AS codigoOrden,
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
                    p.monto AS montoTotal,
                    o.codigo AS codigoOrden,
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
                    p.monto AS montoTotal,
                    o.codigo AS codigoOrden,
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
                    p.monto AS montoTotal,
                    o.codigo AS codigoOrden,
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

    //LISTAR ORDENES PARA USUARIO
    @Query(value = "SELECT \n" +
            "    o.idOrden AS id,\n" +
            "    o.codigo AS CodigoOrden,\n" +
            "    p.monto AS CostoTotal,\n" +
            "    o.fechaCreacion AS fecha,\n" +
            "    eo.nombre AS EstadoOrden,\n" +
            "    CASE \n" +
            "        WHEN o.agentCompra_idUsuario IS NULL THEN 'No asignado'\n" +
            "        ELSE CONCAT(u.nombre, ' ', u.apellido)\n" +
            "    END AS Agente,\n" +
            "    o.valoracionAgente AS Valoracion\n" +
            "FROM \n" +
            "    `dev-TRADO_DB`.Orden o\n" +
            "JOIN \n" +
            "    `dev-TRADO_DB`.Pago p ON o.Pago_idPago = p.idPago\n" +
            "JOIN \n" +
            "    `dev-TRADO_DB`.EstadoOrden eo ON o.estadoOrden_idEstadoOrden = eo.idEstadoOrden\n" +
            "LEFT JOIN \n" +
            "    `dev-TRADO_DB`.Usuario u ON o.agentCompra_idUsuario = u.idUsuario\n" +
            "JOIN \n" +
            "    `dev-TRADO_DB`.Usuario ua ON o.usuario_idUsuario = ua.idUsuario\n" +
            "WHERE \n" +
            "    o.usuario_idUsuario = ? \n" +
            "    AND o.isDeleted = 0;\n", nativeQuery = true)
    List<Object[]> findOrdersByUsuarioIdusuario(Integer idUsuario);

    Optional<Orden> findByCodigo(String codigo);

    List<Orden> findAllByAgentcompraIdusuario(Usuario usuario);

    List<Orden> findAllByUsuarioIdusuario(Usuario idUsuario);

    @Query("SELECT new com.app.tradogt.dto.OrdenEstadoDto(o.fechaCreacion, o.fechaValidacion, o.fechaEnProceso, o.fechaArribo, o.fechaEnAduanas, o.fechaEnRuta, o.fechaRecibido) FROM Orden o WHERE YEAR(o.fechaCreacion) = YEAR(CURRENT_DATE ) AND MONTH(o.fechaCreacion) <= MONTH(CURRENT_DATE )")
    List<OrdenEstadoDto> getOrdenEstado();

    //Listar las ordenes por fecha y estado
    List<Orden> findByEstadoordenIdestadoorden( Optional<EstadoOrden> estadoordenIdestadoorden);

}
