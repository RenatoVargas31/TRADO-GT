package com.app.tradogt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sistema")
public class SistemaController {
    @GetMapping("/loguin")
    public String viewloguin(){return "loguin";}
    @GetMapping("/PassRestore")
    public String viwPassRestore(){return "ContraseñaRestore";}
    @GetMapping("/CreateAcc")
    public String viwCreateAcc(){return "CreateAcc";}

}
