package br.com.animenote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserLoginController {
	
	@GetMapping("/login")
    public String login(String error, String logout, Model model) {
        if (error != null)
            model.addAttribute("error", "Seu nome de usuário e/ou senha são inválidos.");

        if (logout != null)
            model.addAttribute("message", "Logout realizado com sucesso.");

        return "login";
    }
	
}
