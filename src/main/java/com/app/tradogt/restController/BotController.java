package com.app.tradogt.restController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
/*
@RestController
@RequestMapping("/bot")
public class BotController {

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;

    public BotController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestBody String userMessage) {
        // Preparar el cuerpo de la solicitud para enviarlo a API Gateway
        String payload = "{\"message\": \"" + userMessage + "\"}";

        // Configuración de cabeceras (CORS, Content-Type, etc.)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear la solicitud
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        // Realizar la solicitud POST a la API Gateway
        try {
            ResponseEntity<String> response = restTemplate.exchange(apiGatewayUrl, HttpMethod.POST, entity, String.class);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }

}*/
