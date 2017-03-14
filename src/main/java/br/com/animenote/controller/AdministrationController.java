package br.com.animenote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministrationController {
	
	@GetMapping("/administracao")
	public String administration() {
		return "administration";
	}
}
