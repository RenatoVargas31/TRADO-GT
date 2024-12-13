package com.app.tradogt.repository;

import com.app.tradogt.entity.MyFavorite;
import com.app.tradogt.entity.MyFavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyFavoriteRepository extends JpaRepository<MyFavorite, MyFavoriteId> {


    @Query(value = "SELECT pr.nombre AS NombreProducto, " +
            "pr.idProducto AS ID, " +
            "pr.marca AS MARCA, " +
            "pr.codigo AS Codigo, " +
            "CONCAT(pv.nombre, ' ', pv.apellido) AS ProveedorCompleto, " +
            "pr.foto AS Foto, " +
            "pz.cantidad AS Stock, " +
            "pv.tienda AS Tienda, " +
            "pr.precio AS Precio, " +
            "pr.descripcion AS Descripcion, " +
            "pr.color AS Color, " +
            "pv.idProveedor AS ID_Pv " +
            "FROM MyFavorite m " +
            "JOIN ProductoEnZona pz ON pz.producto_idProducto = m.producto_idProducto " +
            "JOIN Zona z ON z.idZona = pz.zona_idZona " +
            "JOIN Usuario u ON u.idUsuario = m.usuario_idUsuario " +
            "JOIN Producto pr ON pr.idProducto = pz.producto_idProducto " +
            "JOIN Proveedor pv ON pv.idProveedor = pr.proveedor_idProveedor " +
            "WHERE u.idUsuario = :idUser AND z.idZona = :idZona " +
            "ORDER BY m.fecha DESC " +
            "LIMIT 5;",
            nativeQuery = true)
    List<Object[]> findFavorites(@Param("idUser") int idUser, @Param("idZona") int idZona);


    //Lista de productos favoritos
    @Query(value = "SELECT pr.nombre AS NombreProducto, " +
            "pr.idProducto AS ID, " +
            "pr.marca AS MARCA, " +
            "pr.codigo AS Codigo, " +
            "CONCAT(pv.nombre, ' ', pv.apellido) AS ProveedorCompleto, " +
            "pr.foto AS Foto, " +
            "pz.cantidad AS Stock, " +
            "pv.tienda AS Tienda, " +
            "pr.precio AS Precio, " +
            "pr.descripcion AS Descripcion, " +
            "pr.color AS Color, " +
            "pv.idProveedor AS ID_Pv " +
            "FROM MyFavorite m " +
            "JOIN ProductoEnZona pz ON pz.producto_idProducto = m.producto_idProducto " +
            "JOIN Zona z ON z.idZona = pz.zona_idZona " +
            "JOIN Usuario u ON u.idUsuario = m.usuario_idUsuario " +
            "JOIN Producto pr ON pr.idProducto = pz.producto_idProducto " +
            "JOIN Proveedor pv ON pv.idProveedor = pr.proveedor_idProveedor " +
            "WHERE u.idUsuario = :idUser AND z.idZona = :idZona " +
            "ORDER BY m.fecha DESC; ",
            nativeQuery = true)
    List<Object[]> findMyFavorites(@Param("idUser") int idUser, @Param("idZona") int idZona);


}
