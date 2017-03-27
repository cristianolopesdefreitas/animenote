package br.com.animenote.controller;

import java.io.IOException;
import java.util.Base64;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.animenote.model.User;
import br.com.animenote.service.UserService;
import br.com.animenote.service.auth.UserValidator;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
    private UserValidator userValidator;
	
	@GetMapping("/")
	public String userTimeline(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
		
		User user = userService.findByUsername(userDetails.getUsername());
		
		model.addAttribute("user", user);
		
		String avatar = null;
		
		if (user.getAvatar() != null) {
			avatar = "data:" + user.getAvatarType() + ";base64," + new String(Base64.getEncoder().encode(user.getAvatar()));
		}
		
		model.addAttribute("avatar", avatar);
		
		return "timeline";
	}
	
	@GetMapping("/minhas-informacoes")
	public String userInformations(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
		
		User user = userService.findByUsername(userDetails.getUsername());
		
		model.addAttribute("user", user);
		
		return "my-informations";
	}
	
	@PostMapping("/minhas-informacoes")
	public String UpdateUserInformations(@Valid User user, BindingResult result, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)auth.getPrincipal();
		
		User currentUser = userService.findByUsername(userDetails.getUsername());
		
		currentUser.setName(user.getName());
		currentUser.setEmail(user.getEmail());
		currentUser.setBirthDate(user.getBirthDate());
		currentUser.setAbout(user.getAbout());
		
		userValidator.validate(currentUser, result);
		
		if (result.hasErrors()) {
			return this.userInformations(model);
		}
		
		userService.updateUser(currentUser);
		
		return "redirect:/minhas-informacoes";
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
		
		if (avatar.getSize() > 1024000) {
			model.addAttribute("error", "O tamanho da imagem não pode passar de 1MB.");
			return this.updateUserAvatar(model);
		}
		
        try {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails)auth.getPrincipal();
			
			User user = userService.findByUsername(userDetails.getUsername());
			
			userService.changeAvatar(user.getId(), avatar.getBytes(), avatar.getContentType());
			
			model.addAttribute("success", "Sua foto foi alterada com sucesso!");
        } catch(IOException e) {
        	model.addAttribute("error", "Ocorreu um erro, não foi possível fazer o upload.");
        }
		
        return this.updateUserAvatar(model);
	}
	
}
