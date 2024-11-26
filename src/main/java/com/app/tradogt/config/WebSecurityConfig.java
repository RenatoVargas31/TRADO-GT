package com.app.tradogt.config;

import com.app.tradogt.entity.EstadoOrden;
import com.app.tradogt.entity.Orden;
import com.app.tradogt.repository.EstadoOrdenRepository;
import com.app.tradogt.repository.OrdenRepository;
import com.app.tradogt.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
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

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Configuration
public class WebSecurityConfig {

    final DataSource dataSource;
    final UsuarioRepository usuarioRepository;
    final CustomAccessDeniedHandler customAccessDeniedHandler;
    final EstadoOrdenRepository estadoOrdenRepository;
    final OrdenRepository ordenRepository;

    public WebSecurityConfig(DataSource dataSource, UsuarioRepository usuarioRepository, CustomAccessDeniedHandler customAccessDeniedHandler, EstadoOrdenRepository estadoOrdenRepository, OrdenRepository ordenRepository) {
        this.dataSource = dataSource;
        this.usuarioRepository = usuarioRepository;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.estadoOrdenRepository = estadoOrdenRepository;
        this.ordenRepository = ordenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsManager usersLogin(DataSource dataSource) {
        return new CustomUserDetailsManager(dataSource);
    }
    @Bean
    public UserDetailsManager userSwitch(DataSource dataSource) {
        return new SwitchUserDetailsManager(dataSource);
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
    public SwitchUserFilter switchUserFilter() {
        // Create a new instance of CustomSwitchUserFilter constructor con parámetros
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(userSwitch(dataSource));
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
                case "SuperAdmin" -> response.sendRedirect("inicio");
                case "Administrador Zonal" -> response.sendRedirect("adminzonal/dashboard");
                case "Agente de Compra" -> {
                    updateOrderStatus();
                    response.sendRedirect("agente/allOrders");
                }
                case "Usuario Final" -> {
                    updateOrderStatus();
                    response.sendRedirect("usuario/inicio");
                }
                default -> response.sendRedirect("/default");
            }
        });
        return filter;
    }

    public void updateOrderStatus() {
        //Obtener la fecha actual
        LocalDate today = LocalDate.now();

        Optional<EstadoOrden> estadoactual = estadoOrdenRepository.findById(3);
        Optional<EstadoOrden> arriboAlPais = estadoOrdenRepository.findById(4);
        Optional<EstadoOrden> aduanas = estadoOrdenRepository.findById(5);
        Optional<EstadoOrden> ruta = estadoOrdenRepository.findById(6);
        Optional<EstadoOrden> recibido = estadoOrdenRepository.findById(7);

        List<Orden> ordensInProcess = ordenRepository.findByEstadoordenIdestadoorden(estadoactual);

        for (Orden orden : ordensInProcess) {
            System.out.println("Orden ID: " + orden.getId());

            // Cambiar el estado de acuerdo a la fecha actual
            if (orden.getFechaArribo() != null && orden.getFechaArribo().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(arriboAlPais.get());
            } else if (orden.getFechaEnAduanas() != null && orden.getFechaEnAduanas().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(aduanas.get());
            } else if (orden.getFechaEnRuta() != null && orden.getFechaEnRuta().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(ruta.get());
            } else if (orden.getFechaRecibido() != null && orden.getFechaRecibido().isEqual(today)) {
                orden.setEstadoordenIdestadoorden(recibido.get());
            }else{
                orden.setEstadoordenIdestadoorden(orden.getEstadoordenIdestadoorden());
            }

            // Guardar la orden actualizada
            ordenRepository.save(orden);
        }
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationFilter customAuthenticationFilter, UsuarioRepository usuarioRepository) throws Exception {
        http
                .userDetailsService(usersLogin(dataSource))
                .addFilterBefore(switchUserFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/libs/**").permitAll()
                        .requestMatchers("/loginForm", "/processLogin", "/crearCuenta","/recuperarPass").permitAll()
                        .requestMatchers("/superadmin", "/superadmin/**").hasAnyAuthority("SuperAdmin")
                        .requestMatchers("/adminzonal", "/adminzonal/**").hasAnyAuthority("Administrador Zonal")
                        .requestMatchers("/agente", "/agente/**").hasAnyAuthority("Agente de Compra")
                        .requestMatchers("/usuario", "/usuario/**").hasAnyAuthority("Usuario Final")
                        .requestMatchers("/loginForm", "/crearCuenta").anonymous()
                        .requestMatchers("/api/consultarDni").permitAll()
                        .requestMatchers("/auth/**").permitAll()
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
                                    case "Agente de Compra" -> {
                                        updateOrderStatus();
                                        response.sendRedirect("agente/allOrders");
                                    }
                                    case "Usuario Final" -> {
                                        updateOrderStatus();
                                        response.sendRedirect("usuario/inicio");
                                    }
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