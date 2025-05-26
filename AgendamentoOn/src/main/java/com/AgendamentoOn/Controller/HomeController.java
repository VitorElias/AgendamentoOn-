package com.AgendamentoOn.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.AgendamentoOn.Security.CustomUserDetails;
import com.AgendamentoOn.Model.Usuario;

@Controller
public class HomeController {

   @GetMapping("/")
public String home(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("Auth: " + auth);
    
    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
        System.out.println("Usuário autenticado");

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Usuario usuario = userDetails.getUsuario();

        System.out.println("Usuário: " + usuario.getNome() + ", " + usuario.getEmail());

        model.addAttribute("nomeUsuario", usuario.getNome());
        model.addAttribute("emailUsuario", usuario.getEmail());
    } else {
        System.out.println("Usuário NÃO autenticado ou anônimo");
    }

    model.addAttribute("titulo", "AgendamentoOn");
    model.addAttribute("subtitulo", "– Studio Irys Lima");
    model.addAttribute("mensagemBoasVindas", "Seja bem-vindo(a) ao sistema de agendamento online.");
    model.addAttribute("descricao", "Com o objetivo de automatizar e otimizar o processo de agendamento permitindo que clientes agendem atendimentos de forma independente, sem a necessidade de contato direto com a profissional.");

    return "index";
}


}
