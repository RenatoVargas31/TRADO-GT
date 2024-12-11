package com.app.tradogt.restController;

import com.app.tradogt.entity.InfoAgente;
import com.app.tradogt.repository.InfoAgenteRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping
public class InfoAgenteRest {
    final InfoAgenteRepository infoAgenteRepository;

    public InfoAgenteRest(InfoAgenteRepository infoAgenteRepository) {
        this.infoAgenteRepository = infoAgenteRepository;
    }

    //Obtener InfoAgente por codigoAduanero
    @GetMapping(value = "/infoAgente/{codeAduanero}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<HashMap<String,Object>> obtenerInfoPorCodeAduanero(@PathVariable("codeAduanero") String codeAduanero){

        HashMap<String,Object> response = new HashMap<>();
        try {
            Optional<InfoAgente> optInfo = infoAgenteRepository.findById(codeAduanero);
            if (optInfo.isPresent()) {
                response.put("result", "success");
                response.put("data", optInfo.get());
                return ResponseEntity.ok(response);
            }else {
                response.put("msg","El código no existe");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e){
            response.put("result","error");
            response.put("msg","parámetros incorrectos");
        }
        return ResponseEntity.badRequest().body(response);
    }
}
