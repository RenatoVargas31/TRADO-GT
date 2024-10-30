package com.app.tradogt.restController;
import com.app.tradogt.daos.DniDao;
import com.app.tradogt.dto.ApiDniRestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DniController {
    @Autowired
    private DniDao dniDao;

    @GetMapping("/api/consultarDni")
    public ApiDniRestDto consultarDni(@RequestParam String dni) {
        return dniDao.buscarDatosPorDni(dni);
    }
}
