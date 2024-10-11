package com.app.tradogt.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                switch (authority.getAuthority()) {
                    case "SuperAdmin":
                        response.sendRedirect("/superadmin/inicio");
                        return;
                    case "Administrador Zonal":
                        response.sendRedirect("/adminzonal/dashboard");
                        return;
                    case "Agente de Compra":
                        response.sendRedirect("/agente/inicio");
                        return;
                    case "Usuario Final":
                        response.sendRedirect("/usuario/inicio");
                        return;
                }
            }
        }
        response.sendRedirect("/access-denied");
    }
}
