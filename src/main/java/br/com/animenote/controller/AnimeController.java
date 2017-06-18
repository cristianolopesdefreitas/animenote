package br.com.animenote.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;

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

import br.com.animenote.constants.AnimeInteractions;
import br.com.animenote.constants.Status;
import br.com.animenote.model.Anime;
import br.com.animenote.model.Role;
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
	
	private User getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		return userService.findByUsername(userDetails.getUsername());
	}
	
	private boolean isAdmin() {
		User user = getLoggedUser();
		
		for (Iterator<Role> iterator = user.getRoles().iterator(); iterator.hasNext();) {
			if ( "ADMIN".equals(iterator.next().getName()) ) {
				return true;
			}
		}
		
		return false;
	}

	@GetMapping("/animes")
	public String findAll(Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("animes", animeService.findAll());

		return "animes";
	}

	@GetMapping("/anime/{id}")
	public String showAnime(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		Anime anime = animeService.findById(id);

		if (anime != null) {
			String animeImage = "data:" + anime.getImageType() + ";base64,"
					+ new String(Base64.getEncoder().encode(anime.getImage()));

			model.addAttribute("animeImage", animeImage);
			model.addAttribute("anime", anime);
		} else {
			model.addAttribute("message", "Anime não encontrado.");
		}

		User user = getLoggedUser();

		UserInteractionAnime interaction = userInteractionAnimeService.findByUserAndAnimeAndStatus(user, anime, Status.A);

		if (interaction != null) {
			model.addAttribute("userInteraction", interaction.getAnimeInteractions());
		} else {
			model.addAttribute("userInteraction", "");
		}
		
		model.addAttribute("loggedUser", user);

		return "anime";
	}

	@GetMapping("/cadastrar-anime")
	public String animeRegistrationScreen(Anime anime, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("anime", anime);
		model.addAttribute("animeCategories", animeCategoryService.findAll());
		model.addAttribute("animeCreators", animeCreatorService.findAll());

		return "anime-registration";
	}

	@PostMapping("/cadastrar-anime")
	public String animeRegistration(@Valid Anime anime, BindingResult result,
			@RequestParam("animeImage") MultipartFile animeImage, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
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

		try {
			anime.setImage(animeImage.getBytes());
			anime.setImageType(animeImage.getContentType());
		} catch (IOException e) {
			model.addAttribute("error", "Ocorreu um erro com o upload da imagem, tente novamente.");
			return this.animeRegistrationScreen(anime, model);
		}

		if (result.hasErrors()) {
			return this.animeRegistrationScreen(anime, model);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		anime.setUser(user);

		animeService.save(anime);

		return "redirect:/animes-cadastrados";
	}

	@GetMapping("/editar-anime/{id}")
	public String editAnimeScreen(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
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
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		Anime savedAnime = animeService.findById(anime.getId());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		if (savedAnime != null && savedAnime.getUser().getId() == user.getId()) {
			anime.setUser(user);
			anime.setImage(savedAnime.getImage());
			anime.setImageType(savedAnime.getImageType());
			anime.setStatus(Status.I);
			animeService.save(anime);
			model.addAttribute("success", "O anime foi editado com sucesso!");
		} else {
			model.addAttribute("error", "Ocorreu um erro, não é possível fazer a edição.");
		}

		return "anime-registration";
	}

	@GetMapping("/alterar-imagem-anime/{id}")
	public String changeAnimeImageScreen(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		Anime anime = animeService.findById(id);

		if (anime != null) {
			model.addAttribute("anime", anime);
		} else {
			model.addAttribute("error", "Anime não encontrado.");
		}

		return "change-anime-image";
	}

	@PostMapping("/alterar-imagem-anime/{id}")
	public String changeAnimeImage(@PathVariable Long id, @RequestParam("animeImage") MultipartFile animeImage,
			Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
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
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		model.addAttribute("user", user);
		model.addAttribute("animes", animeService.findByUser(user));

		return "registered-animes";
	}
	
	@GetMapping("/anime/{animeId}/{interaction}")
	public String interactionAnime(@PathVariable Long animeId, @PathVariable String interaction, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		Anime anime = animeService.findById(animeId);
		
		if ( anime == null ) {
			model.addAttribute("errorMessage", "Anime não encontrado.");
			
			return "error-message";
		}
		
		AnimeInteractions currentInteraction = null;
		
		if ( interaction.equals("ja_vi") ) {
			currentInteraction = AnimeInteractions.JA_VI;
		} else if ( interaction.equals("estou_vendo")) {
			currentInteraction = AnimeInteractions.ESTOU_VENDO;
		} else if ( interaction.equals("quero_ver")) {
			currentInteraction = AnimeInteractions.QUERO_VER;
		}
		
		UserInteractionAnime userInteractionAnime = userInteractionAnimeService.findByUserAndAnime(user, anime);
		
		if (userInteractionAnime != null) {
			if (userInteractionAnime.getAnimeInteractions().equals(currentInteraction)) {
				if ( userInteractionAnime.getStatus().getValue().equals("Ativo") ) {
					userInteractionAnime.setStatus(Status.I);
				} else {
					userInteractionAnime.setStatus(Status.A);
				}
			} else {
				userInteractionAnime.setAnimeInteractions(currentInteraction);
				userInteractionAnime.setStatus(Status.A);
			}
		} else {
			userInteractionAnime = new UserInteractionAnime();
			
			userInteractionAnime.setUser(user);
			userInteractionAnime.setAnime(anime);
			userInteractionAnime.setAnimeInteractions(currentInteraction);
		}
		
		userInteractionAnimeService.saveAndFlush(userInteractionAnime);
		
		return "redirect:/anime/" + animeId;
	}
	
	@GetMapping("/administracao/moderar-animes")
	public String moderateAnime(Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("animes", animeService.findByStatus(Status.I));
		
		return "moderate-anime";
	}
	
	@GetMapping("/administracao/moderar-animes/{id}")
	public String saveModerateAnime(@PathVariable Long id, Model model) {
		Anime anime = animeService.findById(id);
		
		anime.setStatus(Status.A);
		
		animeService.save(anime);
		return "redirect:/administracao/moderar-animes";
	}
}
