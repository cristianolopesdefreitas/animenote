package br.com.animenote.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.animenote.model.User;
import br.com.animenote.service.UserService;

@Controller
public class UserLoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login(String error, String logout, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error", "Seu nome de usuário e/ou senha são inválidos.");
		}

		if (logout != null) {
			model.addAttribute("message", "Logout realizado com sucesso.");
		}

		return "login";
	}

	@GetMapping("/esqueci-minha-senha")
	public String forgotMyPasswordScreen(Model model) {
		return "forgot-my-password";
	}

	@PostMapping("/esqueci-minha-senha")
	public String forgotMyPassword(@RequestParam("email") String email, Model model) {
		User user = userService.findByEmail(email);

		if (user != null) {
			String newPassword = KeyGenerators.string().generateKey();
			userService.changePassword(user.getId(), newPassword);

			try {
				userService.sendForgotPasswordEmail(user, newPassword);
				model.addAttribute("success", "Uma nova senha foi enviada para seu e-mail.");
			} catch (MessagingException e) {
				model.addAttribute("error",
						"Ocorreu um erro ao tentar enviar sua nova senha, tente novamente em instantes.");
			}
		} else {
			model.addAttribute("error", "Este e-mail não está cadastrado.");
		}

		return forgotMyPasswordScreen(model);
	}

}
