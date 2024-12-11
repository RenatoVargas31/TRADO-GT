package com.app.tradogt.daos;

import com.app.tradogt.entity.InfoAgente;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class InfoAgenteDao {

    public InfoAgente obtenerInfoAgente(String codeAduanero) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/infoAgente/" + codeAduanero;
        ResponseEntity<HashMap> response = restTemplate.getForEntity(url, HashMap.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            HashMap<String, Object> responseBody = response.getBody();
            if ("success".equals(responseBody.get("result"))) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.convertValue(responseBody.get("data"), InfoAgente.class);
            }
        }
        return null;
    }
}
