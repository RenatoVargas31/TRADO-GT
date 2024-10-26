package com.app.tradogt.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // Determine if the user is an "Agente de Compra"
        if (isAgenteDeCompra(username)) {
            // Authentication with "codigoDespachador"
            username = request.getParameter("codigoDespachador");
            if (username == null || username.isEmpty()) {
                throw new AuthenticationException("codigoDespachador is required for Agente de Compra") {};
            }
        } else {
            // Authentication with email
            username = request.getParameter("correo");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private boolean isAgenteDeCompra(String username) {
        String role = jdbcTemplate.queryForObject(
                "SELECT R.nombre FROM Usuario U INNER JOIN Rol R ON U.rol_idRol = R.idRol WHERE U.correo = ? OR U.codigoDespachador = ?",
                new Object[]{username, username},
                String.class
        );
        return "Agente de Compra".equals(role);
    }
}
