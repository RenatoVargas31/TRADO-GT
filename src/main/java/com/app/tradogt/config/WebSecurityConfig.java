package com.app.tradogt.config;

import com.app.tradogt.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    final DataSource dataSource;
    final UsuarioRepository usuarioRepository;
    final CustomAccessDeniedHandler customAccessDeniedHandler;

    public WebSecurityConfig(DataSource dataSource, UsuarioRepository usuarioRepository, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.dataSource = dataSource;
        this.usuarioRepository = usuarioRepository;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsManager usersLogin(DataSource dataSource) {
        return new CustomUserDetailsManager(dataSource, true);
    }

    //Impersonate///////////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean
    public SwitchUserFilter switchUserFilter() {
        // Create a new instance of CustomSwitchUserFilter constructor con parámetros
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(new CustomUserDetailsManager(dataSource, false));
        filter.setSwitchUserUrl("/superadmin/impersonate");
        filter.setExitUserUrl("/superadmin/exit");
        filter.setSuccessHandler((request, response, authentication) -> {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioAutenticado",usuarioRepository.findByCorreo(authentication.getName()));
            session.setAttribute("impersonation", true);

            String rol = "";
            for (GrantedAuthority role : authentication.getAuthorities()) {
                rol = role.getAuthority();
                break;
            }
            switch (rol) {
                case "SuperAdmin" -> response.sendRedirect("/superadmin/inicio");
                case "Administrador Zonal" -> response.sendRedirect("/adminzonal/dashboard");
                case "Agente de Compra" -> {
                    response.sendRedirect("/agente/allOrders");
                }
                case "Usuario Final" -> {
                    response.sendRedirect("/usuario/inicio");
                }
                default -> response.sendRedirect("/default");
            }
        });
        return filter;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(usersLogin(dataSource))
                .addFilterBefore(switchUserFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/libs/**").permitAll()
                        .requestMatchers("/loginForm", "/processLogin", "/crearCuenta", "/recuperarPass", "/request-password-reset", "/verificarCode", "/verify-codE", "/reset-password-form/**", "/reset-password", "/change-temporal-pass", "/change-temporalPass", "/change-temporal-pass-agente").permitAll()
                        .requestMatchers("/superadmin", "/superadmin/**").hasAnyAuthority("SuperAdmin")
                        .requestMatchers("/adminzonal", "/adminzonal/**").hasAnyAuthority("Administrador Zonal")
                        .requestMatchers("/agente", "/agente/**").hasAnyAuthority("Agente de Compra")
                        .requestMatchers("/usuario", "/usuario/**").hasAnyAuthority("Usuario Final")
                        .requestMatchers("/loginForm", "/crearCuenta").anonymous()
                        .requestMatchers("/api/consultarDni", "/infoAgente/**").permitAll()
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

                            HttpSession session = request.getSession();
                            session.setAttribute("usuarioAutenticado", usuarioRepository.findByCorreo(authentication.getName()));

                            if (defaultSavedRequest != null) {
                                String targetURL = defaultSavedRequest.getRedirectUrl();
                                logger.info("Redirecting to saved request URL: {}", targetURL);
                                new DefaultRedirectStrategy().sendRedirect(request, response, targetURL);
                            } else {
                                String rol = "";
                                for (GrantedAuthority role : authentication.getAuthorities()) {
                                    rol = role.getAuthority();
                                    break;
                                }
                                switch (rol) {
                                    case "SuperAdmin" -> {
                                        logger.info("Redirecting to SuperAdmin home");
                                        response.sendRedirect("superadmin/inicio");
                                    }
                                    case "Administrador Zonal" -> {
                                        logger.info("Redirecting to Administrador Zonal dashboard");
                                        response.sendRedirect("adminzonal/dashboard");
                                    }
                                    case "Agente de Compra" -> {
                                        logger.info("Redirecting to Agente de Compra orders");
                                        response.sendRedirect("agente/allOrders");
                                    }
                                    case "Usuario Final" -> {
                                        logger.info("Redirecting to Usuario Final home");
                                        response.sendRedirect("usuario/inicio");
                                    }
                                }
                            }
                        })
                        .failureHandler((request, response, exception) -> {
                            logger.error("Login failed: {}", exception.getMessage());
                            response.sendRedirect("/loginForm?error=true");
                        })
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/loginForm")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .exceptionHandling((exceptions) -> exceptions.accessDeniedHandler(customAccessDeniedHandler));;
        return http.build();
    }
}