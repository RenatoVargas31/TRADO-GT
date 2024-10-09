package com.app.tradogt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {

    final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String sql1 = "SELECT correo, contrasena, isAccepted FROM Usuario WHERE correo = ?";
        String sql2 = "SELECT U.correo, Rol.nombre FROM Usuario U"
                + "INNER JOIN Rol R ON (U.idRol = R.idRol)"
                + "WHERE U.correo = ? and U.isAccepted = 1";
        users.setUsersByUsernameQuery(sql1);
        users.setAuthoritiesByUsernameQuery(sql2);
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/processLogin").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/processLogin")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                );

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/superadmin", "/superadmin/**").hasAnyAuthority("SuperAdmin")
                .anyRequest().permitAll()
        );
        return http.build();
    }
}