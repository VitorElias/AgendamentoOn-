package com.AgendamentoOn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.AgendamentoOn.Model.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Pegando o usuário da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuario");

            model.addAttribute("username", usuario.getNome());
            System.out.println("Usuário logado (CLIENTE): " + usuario.getEmail());
            return "home";
     
    }
}
