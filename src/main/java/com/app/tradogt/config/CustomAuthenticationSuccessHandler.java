package com.app.tradogt.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Imprimir detalles del usuario
        System.out.println("Usuario autenticado: " + authentication.getName());

        // Imprimir roles del usuario
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("Rol: " + authority.getAuthority());
        }
    }
}