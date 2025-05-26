package com.AgendamentoOn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PoliticaController {

    @GetMapping("/politicas")
    public String mostrarPolitica() {

        return "politicas";
    }
}