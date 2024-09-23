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
    public String viewPassRestore(){return "ContraseñaRestore";}
    @GetMapping("/PassRestoreConfirm")
    public String viewPassRestoreConfirm(){return "ContraseñaRestore-confirm";}
    @GetMapping("/CreateAcc")
    public String viewCreateAcc(){return "CreateAcc";}
    @GetMapping("/CreateAccConfirm")
    public String viewCreateAccConfirm(){return "CreateAcc-confirm";}
    @GetMapping("/RestablecerPass")
    public String viewPass(){return "PassRestore";}
    @GetMapping("/RestablecerPassConfirm")
    public String viewPassConfirm(){return "PassRestore-confirm";}

}
