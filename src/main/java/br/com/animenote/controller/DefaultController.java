package br.com.animenote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
	
	@GetMapping("/error")
	public String pageNotFound() {
		return "404";
	}
}
