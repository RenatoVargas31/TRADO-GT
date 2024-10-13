package com.app.tradogt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {

    final DataSource dataSource;
    final CustomAccessDeniedHandler customAccessDeniedHandler;

    public WebSecurityConfig(DataSource dataSource, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.dataSource = dataSource;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        CustomUserDetailsManager user = new CustomUserDetailsManager(dataSource);
        user.setUsersByUsernameQuery("SELECT correo, contrasena, isActivated, nombre, apellido, codigoDespachador FROM Usuario WHERE correo = ? OR codigoDespachador = ?");
        user.setAuthoritiesByUsernameQuery(
                "SELECT U.correo, R.nombre " +
                        "FROM Usuario U " +
                        "INNER JOIN Rol R ON U.rol_idRol = R.idRol " +
                        "WHERE (U.correo = ? OR U.codigoDespachador = ?) AND U.isActivated = 1"
        );
        return user;
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationFilter customAuthenticationFilter) throws Exception {
        http
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "libs/**").permitAll()
                        .requestMatchers("/loginForm", "/processLogin", "/crearCuenta").permitAll()
                        .requestMatchers("/superadmin", "/superadmin/**").hasAnyAuthority("SuperAdmin")
                        .requestMatchers("/adminzonal", "/adminzonal/**").hasAnyAuthority("Administrador Zonal")
                        .requestMatchers("/agente", "/agente/**").hasAnyAuthority("Agente de Compra")
                        .requestMatchers("/usuario", "/usuario/**").hasAnyAuthority("Usuario Final")
                        .requestMatchers("/loginForm", "/crearCuenta").anonymous()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/processLogin")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            DefaultSavedRequest defaultSavedRequest =
                                    (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                            if (defaultSavedRequest != null) {
                                String targetURL = defaultSavedRequest.getRedirectUrl();
                                new DefaultRedirectStrategy().sendRedirect(request, response, targetURL);
                            } else {
                                String rol = "";
                                for (GrantedAuthority role : authentication.getAuthorities()) {
                                    rol = role.getAuthority();
                                    break;
                                }
                                switch (rol) {
                                    case "SuperAdmin" -> response.sendRedirect("superadmin/inicio");
                                    case "Administrador Zonal" -> response.sendRedirect("adminzonal/dashboard");
                                    case "Agente de Compra" -> response.sendRedirect("agente/inicio");
                                    case "Usuario Final" -> response.sendRedirect("usuario/inicio");
                                }
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/loginForm")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .exceptionHandling((exceptions) -> exceptions.accessDeniedHandler(customAccessDeniedHandler));

        return http.build();
    }
}