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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionAnime;
import br.com.animenote.service.AnimeCategoryService;
import br.com.animenote.service.AnimeCreatorService;
import br.com.animenote.service.AnimeService;
import br.com.animenote.service.UserInteractionAnimeService;
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
	
	@Autowired
	private UserInteractionAnimeService userInteractionAnimeService;

	@GetMapping("/animes")
	public String findAll(Model model) {
		model.addAttribute("animes", animeService.findAll());

		return "animes";
	}

	@GetMapping("/anime/{id}")
	public String showAnime(@PathVariable Long id, Model model) {
		Anime anime = animeService.findById(id);

		if (anime != null) {
			String animeImage = "data:" + anime.getImageType() + ";base64,"
					+ new String(Base64.getEncoder().encode(anime.getImage()));

			model.addAttribute("animeImage", animeImage);
			model.addAttribute("anime", anime);
		} else {
			model.addAttribute("message", "Anime não encontrado.");
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		
		UserInteractionAnime interaction = userInteractionAnimeService.findByUserAndAnime(user, anime);
		
		if (interaction !=  null) {
			model.addAttribute("userInteraction", interaction.getAnimeInteractions());
		} else {
			model.addAttribute("userInteraction", "");
		}

		return "anime";
	}

	@GetMapping("/cadastrar-anime")
	public String animeRegistrationScreen(Anime anime, Model model) {
		model.addAttribute("anime", anime);
		model.addAttribute("animeCategories", animeCategoryService.findAll());
		model.addAttribute("animeCreators", animeCreatorService.findAll());

		return "anime-registration";
	}

	@PostMapping("/cadastrar-anime")
	public String animeRegistration(@Valid Anime anime, BindingResult result,
			@RequestParam("animeImage") MultipartFile animeImage, Model model) {
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

		if (result.hasErrors()) {
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

		animeService.save(anime);

		return "redirect:/animes-cadastrados";
	}

	@GetMapping("/editar-anime/{id}")
	public String editAnimeScreen(@PathVariable Long id, Model model) {
		Anime anime = animeService.findById(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		if (anime.getUser().getId() == user.getId()) {
			model.addAttribute("anime", anime);
			model.addAttribute("animeCategories", animeCategoryService.findAll());
			model.addAttribute("animeCreators", animeCreatorService.findAll());
		} else {
			model.addAttribute("error", "Anime não encontrado.");
		}

		return "anime-registration";
	}

	@PostMapping("/editar-anime/{id}")
	public String editAnime(@Valid Anime anime, BindingResult result, Model model) {
		Anime savedAnime = animeService.findById(anime.getId());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		
		if (savedAnime != null && savedAnime.getUser().getId() == user.getId()) {
			anime.setUser(user);
			animeService.save(anime);
			model.addAttribute("success", "O anime foi editado com sucesso!");
		} else {
			model.addAttribute("error", "Ocorreu um erro, não é possível fazer a edição.");
		}

		return "anime-registration";
	}
	
	@GetMapping("/alterar-imagem-anime/{id}")
	public String changeAnimeImageScreen(@PathVariable Long id, Model model) {
		Anime anime = animeService.findById(id);
		
		if (anime != null) {
			model.addAttribute("anime", anime);
		} else {
			model.addAttribute("error", "Anime não encontrado.");
		}
		
		return "change-anime-image";
	}
	
	@PostMapping("/alterar-imagem-anime/{id}")
	public String changeAnimeImage(@PathVariable Long id, @RequestParam("animeImage") MultipartFile animeImage, Model model) {
		Anime savedAnime = animeService.findById(id);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		
		if (savedAnime != null && savedAnime.getUser().getId() == user.getId()) {
			if (animeImage.isEmpty() || animeImage.getSize() == 0) {
				model.addAttribute("error", "Por favor insira uma imagem.");
				return this.changeAnimeImageScreen(id, model);
			}

			if (!(animeImage.getContentType().toLowerCase().equals("image/jpg")
					|| animeImage.getContentType().toLowerCase().equals("image/jpeg")
					|| animeImage.getContentType().toLowerCase().equals("image/png"))) {
				model.addAttribute("error", "Tipos de arquivo suportados apenas: jpg/png");
				return this.changeAnimeImageScreen(id, model);
			}

			if (animeImage.getSize() > 1024000) {
				model.addAttribute("error", "O tamanho da imagem não pode passar de 1MB.");
				return this.changeAnimeImageScreen(id, model);
			}
			
			try {
				savedAnime.setImage(animeImage.getBytes());
				savedAnime.setImageType(animeImage.getContentType());

				animeService.save(savedAnime);
			} catch (IOException e) {
				model.addAttribute("error", "Ocorreu um erro com o upload da imagem, tente novamente.");
				return this.changeAnimeImageScreen(id, model);
			}
		}
		
		return this.changeAnimeImageScreen(id, model);
	}

	@GetMapping("/animes-cadastrados")
	public String add(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		model.addAttribute("user", user);
		model.addAttribute("animes", animeService.findByUser(user));

		return "registered-animes";
	}
}
