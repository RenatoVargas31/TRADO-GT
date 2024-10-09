package com.app.tradogt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;
import java.util.EnumSet;

@Configuration
public class WebSecurityConfig {

    final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Bean para la codificación de contraseñas usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // Configuración de UserDetailsManager para obtener usuarios de la base de datos
    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        // Query para obtener el usuario y su contraseña
        String sql1 = "SELECT correo, contrasena, isActivated FROM Usuario WHERE correo = ?";

        // Query para obtener los roles del usuario
        String sql2 = "SELECT U.correo, R.nombre FROM Usuario U " +
                "INNER JOIN Rol R ON U.rol_idRol = R.idRol " +
                "WHERE U.correo = ? AND U.isActivated = 1";

        users.setUsersByUsernameQuery(sql1);
        users.setAuthoritiesByUsernameQuery(sql2);
        return users;
    }

    // Configuración del SecurityFilterChain para el manejo de la seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Definición de rutas y permisos
                .authorizeHttpRequests((requests) -> requests
                        //Permisos de recursos
                        .requestMatchers("/css/**", "/js/**", "/images/**","/fonts/**","libs/**").permitAll()
                        // Rutas públicas
                        .requestMatchers("/loginForm", "/processLogin", "/sistema/CreateAcc", "/sistema/PassRestore").permitAll()
                        // Solo accesibles por el rol "SuperAdmin"
                        .requestMatchers("/superadmin", "/superadmin/**").hasAnyAuthority("SuperAdmin")
                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                // Configuración del login
                .formLogin((form) -> form
                        .loginPage("/loginForm") // Página de inicio de sesión
                        .loginProcessingUrl("/processLogin") // URL que procesa el login
                        .usernameParameter("correo") // Nombre del campo para el usuario
                        .passwordParameter("password") // Nombre del campo para la contraseña
                        // Handler para redireccionar dependiendo del rol del usuario
                        .successHandler((request, response, authentication) -> {
                            DefaultSavedRequest defaultSavedRequest =
                                    (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                            if (defaultSavedRequest != null) {
                                String targetURL = defaultSavedRequest.getRedirectUrl();
                                new DefaultRedirectStrategy().sendRedirect(request, response, targetURL);
                            } else {
                                // Determinación del rol del usuario autenticado
                                String rol = "";
                                for (GrantedAuthority role : authentication.getAuthorities()) {
                                    rol = role.getAuthority();
                                    break;
                                }
                                // Redirección según el rol
                                switch (rol) {
                                    case "SuperAdmin" -> response.sendRedirect("superadmin/inicio");
                                    case "Administrador Zonal" -> response.sendRedirect("adminzonal/dashboard");
                                    case "Agente de Compra" -> response.sendRedirect("agente/inicio");
                                    default -> response.sendRedirect("usuario/inicio");
                                }
                            }
                        })
                )
                // Configuración del logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/sistema/loguin") // URL de redirección después de logout
                        .deleteCookies("JSESSIONID") // Borrar cookies de sesión
                        .invalidateHttpSession(true) // Invalidar la sesión
                );

        return http.build();
    }
}