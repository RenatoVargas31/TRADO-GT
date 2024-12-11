package com.app.tradogt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //Con esta anotación habilitamos las tareas programadas
public class TradoGtApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradoGtApplication.class, args);
    }

}
