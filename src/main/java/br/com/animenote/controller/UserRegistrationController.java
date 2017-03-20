package br.com.animenote.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.animenote.model.User;
import br.com.animenote.service.UserService;
import br.com.animenote.service.auth.SecurityService;
import br.com.animenote.service.auth.UserValidator;

@Controller
public class UserRegistrationController {
	@Autowired
	private UserService userService;

    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private SecurityService securityService;
	
	@GetMapping("/cadastro")
	public String userRegistration(User user, Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
		    return "redirect:/";
		}
		
		model.addAttribute("user", user);
		
		return "user-registration";
	}

	@PostMapping("/cadastro")
	public String save(@Valid User user, BindingResult result, Model model) {
		userValidator.validate(user, result);
		
		if (result.hasErrors()) {
			return this.userRegistration(user, model);
		}
		
		userService.saveUser(user);
		
		securityService.autologin(user.getUsername(), user.getPasswordConfirmation());
		
		return "redirect:/";
	}
	
	@GetMapping("/confirmacao-de-conta/{username}")
	public String confirmAccount(@PathVariable("username") String username) {
		
		if (userService.confirmAccount(username)) {
			return "confirm-account-success";
		} else {
			return "confirm-account-error";
		}
	}
	
}
