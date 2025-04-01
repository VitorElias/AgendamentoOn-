package com.AgendamentoOn.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.AgendamentoOn.Enum.Role;
import com.AgendamentoOn.Model.Usuario;
import com.AgendamentoOn.Service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CadastroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @GetMapping("/cadastro")
    public String exibirFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro"; 
    }

    @PostMapping("/cadastro")
    public String cadastrar(@Validated Usuario usuario, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "cadastro"; 
        }

        try {

            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

            usuario.setRole(Role.CLIENTE);

            usuarioService.Salvar(usuario);


            session.setAttribute("usuario", usuario);


            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao cadastrar usuário.");
            return "cadastro"; 
        }
    }
}