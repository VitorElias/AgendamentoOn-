package com.AgendamentoOn.Controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.AgendamentoOn.Model.Usuario;
import com.AgendamentoOn.Security.CustomUserDetails;
import com.AgendamentoOn.Service.CustomUserDetailsService;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public LoginController(AuthenticationManager authenticationManager, 
                         CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }
@GetMapping("/login")
public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                            Model model) {
    model.addAttribute("pageTitle", "Login - AgendamentoOn");

    if (error != null) {
        model.addAttribute("error", "E-mail ou senha inválidos");
    }
    return "login";
}

@GetMapping("/login-success")
public String loginSuccess(Model model) {
    model.addAttribute("pageTitle", "Login - AgendamentoOn");
    model.addAttribute("success", "Login realizado com sucesso!");
    return "login";
}




}