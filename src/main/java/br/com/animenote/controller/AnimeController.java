package br.com.animenote.controller;

import java.io.IOException;

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

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.service.AnimeCategoryService;
import br.com.animenote.service.AnimeCreatorService;
import br.com.animenote.service.AnimeService;
import br.com.animenote.service.UserService;

@Controller
public class AnimeController {
	@Autowired
	private AnimeService animeService;

	@Autowired
	private UserService userService;

	@Autowired
	private AnimeCategoryService animeCategoryService;

	@Autowired
	private AnimeCreatorService animeCreatorService;

	@GetMapping("/animes")
	public String findAll(Model model) {
		model.addAttribute("animes", animeService.findAll());

		return "animes";
	}

	@GetMapping("/cadastrar-anime")
	public String animeRegistrationScreen(Anime anime, Model model) {
		model.addAttribute("anime", anime);
		model.addAttribute("animeCategories", animeCategoryService.findAll());
		model.addAttribute("animeCreators", animeCreatorService.findAll());

		return "anime-registration";
	}

	@PostMapping("/cadastrar-anime")
	public String animeRegistration(@Valid Anime anime, BindingResult result, @RequestParam("animeImage") MultipartFile animeImage, Model model) {
		if (result.hasErrors()) {
			return this.animeRegistrationScreen(anime, model);
		}
		
		if (animeImage.isEmpty() || animeImage.getSize() == 0) {
			model.addAttribute("error", "Por favor insira uma imagem.");
			return this.animeRegistrationScreen(anime, model);
		}

		if (!(animeImage.getContentType().toLowerCase().equals("image/jpg")
				|| animeImage.getContentType().toLowerCase().equals("image/jpeg")
				|| animeImage.getContentType().toLowerCase().equals("image/png"))) {
			model.addAttribute("error", "Tipos de arquivo suportados apenas: jpg/png");
			return this.animeRegistrationScreen(anime, model);
		}

		if (animeImage.getSize() > 1024000) {
			model.addAttribute("error", "O tamanho da imagem não pode passar de 1MB.");
			return this.animeRegistrationScreen(anime, model);
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		
		anime.setUser(user);
		
		try {
			anime.setImage(animeImage.getBytes());
			anime.setImageType(animeImage.getContentType());
	
			animeService.save(anime);
		} catch (IOException e) {
			model.addAttribute("error", "Ocorreu um erro com o upload da imagem, tente novamente.");
			return this.animeRegistrationScreen(anime, model);
		}

		return "redirect:/animes-cadastrados";
	}

	@GetMapping("/animes-cadastrados")
	public String add(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		
		model.addAttribute("anime", animeService.findByUserId(user.getId()));

		return "registered-animes";
	}
}
