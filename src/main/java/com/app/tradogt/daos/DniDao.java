package com.app.tradogt.daos;

import com.app.tradogt.dto.ApiDniRestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class DniDao {

    private static final String API_URL = "https://api.apis.net.pe/v2/reniec/dni?numero=";
    private static final String TOKEN = "apis-token-11275.DkI6ciCRKXmYsdI5d8Wimlre3jouj4Ys";

    public ApiDniRestDto buscarDatosPorDni(String dni){

        //ApiDniRestDto apiDniRestDto = null;
        ApiDniRestDto apiDniRestDto = new ApiDniRestDto();

        RestTemplate restTemplate = new RestTemplate();

        // Configura los headers con el Bearer Token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = API_URL + dni;

        // Realiza la solicitud GET con el Bearer Token
        //ResponseEntity<ApiDniRestDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, ApiDniRestDto.class);

        /*if (response.getStatusCode().is2xxSuccessful()) {
            apiDniRestDto = response.getBody();
        }*/
        try {
            ResponseEntity<ApiDniRestDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, ApiDniRestDto.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                apiDniRestDto = response.getBody();
            }
        } catch (HttpClientErrorException e) {
            // Manejo del error específico cuando el DNI no es válido
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                // Procesa el mensaje de error específico
                apiDniRestDto.setMessage("DNI no válido");  // Usa un mensaje personalizado o extrae el mensaje de la respuesta
            } else {
                // Maneja otros errores aquí, si es necesario
                apiDniRestDto.setMessage("DNI no válido");
            }
        }
        return apiDniRestDto;
    }
}
