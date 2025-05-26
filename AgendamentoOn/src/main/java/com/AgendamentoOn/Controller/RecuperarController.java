package com.AgendamentoOn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecuperarController {
 
    @GetMapping("/recuperar")
    public String mostrarPaginaRecuperar() {
        return "recuperar"; 
    }

}
