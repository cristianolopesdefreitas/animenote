package br.com.animenote.controller;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.animenote.model.User;
import br.com.animenote.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String userRegistration(User user, Model model) {
		model.addAttribute("user", user);
		
		return "timeline";
	}
	
	@PostMapping("/avatar-upload")
	public String avatarUpload(@RequestParam("avatar") MultipartFile avatar) throws IOException {
		
		
		return "redirect:/";
	}
	
}
