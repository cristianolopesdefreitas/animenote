package br.com.animenote.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.animenote.model.User;
import br.com.animenote.service.UserService;

@Controller
public class UserController {
	private static final String avatarPath = "src/main/resources/static/images/uploads/";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServletContext context;
	
	@GetMapping("/")
	public String userTimeline(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
		
		User user = userService.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		
		return "timeline";
	}
	
	@GetMapping("/atualizar-dados-de-usuario")
	public String updateUserData(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
		
		User user = userService.findByUsername(userDetails.getUsername());
		
		model.addAttribute("user", user);
		
		return "complete-user-registration";
	}
	
	@GetMapping("/atualizar-avatar")
	public String updateUserAvatar(Model model) {		
		return "avatar-upload";
	}
	
	@PostMapping("/atualizar-avatar")
	public String save(@RequestParam("avatar") MultipartFile avatar, Model model) {
		
		if (avatar.isEmpty() || avatar.getSize() == 0) {
			model.addAttribute("error", "Por favor insira uma imagem.");
			return this.updateUserAvatar(model);
		}
		
		if (!(avatar.getContentType().toLowerCase().equals("image/jpg")
				|| avatar.getContentType().toLowerCase().equals("image/jpeg")
				|| avatar.getContentType().toLowerCase().equals("image/png"))) {
			model.addAttribute("error", "Tipos de arquivo suportados apenas: jpg/png");
			return this.updateUserAvatar(model);
		}
		
		if (avatar.getSize() > 512000) {
			model.addAttribute("error", "O tamanho da imagem não pode passar de 500KB.");
			return this.updateUserAvatar(model);
		}
		
		String imageName = new BigInteger(130, new SecureRandom()).toString(32);
		String imageType = avatar.getContentType().toLowerCase().equals("image/png") ? ".png" : ".jpg";
		String completePath = context.getContextPath() + avatarPath;
		
		while (new File(completePath + imageName + imageType).exists()) {
			imageName = new BigInteger(130, new SecureRandom()).toString(32);
		}
		
		try {
            byte[] bytes = avatar.getBytes();
            Path path = Paths.get(completePath + imageName + imageType);
            Files.write(path, bytes);
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		UserDetails userDetails = (UserDetails)auth.getPrincipal();
    		
    		User user = userService.findByUsername(userDetails.getUsername());
    		
    		userService.changeAvatar(user.getId(), imageName + imageType, completePath);
    		
    		model.addAttribute("success", "Sua foto foi alterada com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return "avatar-upload";
	}
	
}
