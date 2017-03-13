package br.com.animenote.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.animenote.model.User;
import br.com.animenote.service.UserService;

@Controller
public class UserRegistrationController {
	@Autowired
	private UserService service;
	
	@GetMapping("/user-registration")
	public String userRegistration(User user, Model model) {
		model.addAttribute("user", user);
		
		return "user-registration";
	}

	@PostMapping("/user-registration")
	public String save(@Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return this.userRegistration(user, model);
		}
		
		service.saveAndFlush(user);
		
		return "redirect:/user-successfully-registered";
	}
	
	@GetMapping("/user-successfully-registered")
	public String success() {
		return "user-successfully-registered";
	}
}
