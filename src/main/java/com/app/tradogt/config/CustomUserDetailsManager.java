package com.app.tradogt.config;

import com.app.tradogt.entity.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsManager extends JdbcUserDetailsManager {

    private boolean filterByActivate;

    public CustomUserDetailsManager(DataSource dataSource, boolean filterByActivate) {
        super.setDataSource(dataSource);
        this.filterByActivate = filterByActivate;
    }

    @Override
    protected List<UserDetails> loadUsersByUsername(String username) {
        String query;
        if(filterByActivate){
            query = "SELECT correo, contrasena, isActivated, nombre, apellido, codigoDespachador FROM Usuario WHERE (correo = ? OR codigoDespachador = ?) AND isActivated = 1";
            return getJdbcTemplate().query(query, new Object[]{username, username},
                    (ResultSet rs, int rowNum) -> {
                        String correo = rs.getString("correo");
                        String contrasena = rs.getString("contrasena");
                        boolean isActivated = rs.getBoolean("isActivated");
                        String nombre = rs.getString("nombre");
                        String apellido = rs.getString("apellido");
                        String codigoDespachador = rs.getString("codigoDespachador");
                        return new CustomUserDetails(correo, contrasena, isActivated, nombre, apellido, codigoDespachador, new ArrayList<>());
                    }
            );
        }else{
            query = "SELECT correo, contrasena, nombre, apellido, codigoDespachador FROM Usuario WHERE correo = ? OR codigoDespachador = ?";
            return getJdbcTemplate().query(
                    query, new Object[]{username, username},
                    (ResultSet rs, int rowNum) -> {
                        String correo = rs.getString("correo");
                        String contrasena = rs.getString("contrasena");
                        String nombre = rs.getString("nombre");
                        String apellido = rs.getString("apellido");
                        String codigoDespachador = rs.getString("codigoDespachador");
                        return new CustomUserDetails(correo, contrasena, true, nombre, apellido, codigoDespachador, new ArrayList<>());
                    }
            );
        }

    }

    @Override
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        String query;
        if(filterByActivate){
            query = "SELECT U.correo, R.nombre FROM Usuario U INNER JOIN Rol R ON U.rol_idRol = R.idRol WHERE (U.correo = ? OR U.codigoDespachador = ?) AND U.isActivated = 1";
        }else{
            query = "SELECT U.correo, R.nombre FROM Usuario U INNER JOIN Rol R ON U.rol_idRol = R.idRol WHERE U.correo = ? OR U.codigoDespachador = ?";
        }
        return getJdbcTemplate().query(
                query, new Object[]{username, username},
                (ResultSet rs, int rowNum) -> {
                    String authority = rs.getString("nombre");
                    return (GrantedAuthority) () -> authority;
                }
        );
    }

    @Override
    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userFromUserQuery;
        return new CustomUserDetails(
                customUserDetails.getUsername(),
                customUserDetails.getPassword(),
                customUserDetails.isEnabled(),
                customUserDetails.getNombre(),
                customUserDetails.getApellido(),
                customUserDetails.getCodigoDespachador(),
                combinedAuthorities
        );
    }
}