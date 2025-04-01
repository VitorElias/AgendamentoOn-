package com.AgendamentoOn.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.AgendamentoOn.Model.Usuario;
import com.AgendamentoOn.Service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("usuario") // Mantém o usuário na sessão
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            System.out.println("Usuário logado: " + usuario.getEmail()); 
        } else {
            System.out.println("Nenhum usuário na sessão.");
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<Usuario> usuarioOpt = usuarioService.findByEmail(username);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // 🔹 Salvar o usuário na sessão
                model.addAttribute("usuario", usuario);

                return "redirect:/home";
            } else {
                model.addAttribute("error", "Usuário não encontrado.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Usuário ou senha inválidos.");
            return "login";
        }
    }
}