package com.AgendamentoOn.Controller;

import com.AgendamentoOn.Model.Usuario;
import com.AgendamentoOn.Service.UsuarioService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

@Controller
public class CadastroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/cadastrar")
    public String showCadastroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("pageTitle", "Cadastro de Cliente");
        return "cadastrar"; 
    }

    @PostMapping("/cadastrar")
public String processCadastro(@Valid Usuario usuario, BindingResult bindingResult, Model model) {

    usuarioService.salvar(usuario, bindingResult); // valida e salva

    if (bindingResult.hasErrors()) {
        model.addAttribute("pageTitle", "Cadastro de Cliente");
        return "cadastrar";
    }

    // Autenticar o usuário depois de cadastrado
    UsernamePasswordAuthenticationToken authToken = 
        new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());

    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    model.addAttribute("successMessage", "Cliente cadastrado com sucesso!");
    return "redirect:/";
}
}