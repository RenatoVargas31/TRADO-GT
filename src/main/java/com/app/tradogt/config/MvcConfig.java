package com.app.tradogt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/reseñasUsuarios/**")
                .addResourceLocations("file:/C:/Users/Sergio/Desktop/CHAMBEAR/TRADO-GT/uploads/reseñasUsuarios/");
    }
}
