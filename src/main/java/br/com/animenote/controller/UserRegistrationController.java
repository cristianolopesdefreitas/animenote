package br.com.animenote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.animenote.service.UserService;

@Controller
public class UserRegistrationController {
	@Autowired
	private UserService userService;

	//@PostMapping("/user-registration")
	//public String save(@Valid User user, BindingResult result, Model model) {
		
	//}
}
