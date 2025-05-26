package com.AgendamentoOn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TermoController {

    @GetMapping("/termos")
    public String mostrarTermo() {

        return "termos";
    }
}
