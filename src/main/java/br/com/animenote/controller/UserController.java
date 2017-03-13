package br.com.animenote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.animenote.model.User;

@Controller
public class UserController {
	
	@GetMapping("/")
	public String userRegistration(User user, Model model) {
		model.addAttribute("user", user);
		
		return "timeline";
	}
	
}
