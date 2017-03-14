package br.com.animenote.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserLoginController {
	
	@GetMapping("/login")
    public String login(String error, String logout, Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
		    return "redirect:/";
		}
		
        if (error != null) {
            model.addAttribute("error", "Login.error");
        }

        if (logout != null) {
            model.addAttribute("message", "Logout.success");
        }

        return "login";
    }
	
}
