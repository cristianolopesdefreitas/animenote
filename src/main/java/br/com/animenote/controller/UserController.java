package br.com.animenote.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.animenote.constants.Status;
import br.com.animenote.model.Anime;
import br.com.animenote.model.Role;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionAnime;
import br.com.animenote.model.UserPost;
import br.com.animenote.model.UserRelationship;
import br.com.animenote.service.AnimeService;
import br.com.animenote.service.UserInteractionAnimeService;
import br.com.animenote.service.UserPostService;
import br.com.animenote.service.UserRelationshipService;
import br.com.animenote.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	UserPostService userPostService;
	
	@Autowired
	AnimeService animeService;
	
	@Autowired
	UserRelationshipService userRelationshipService;
	
	@Autowired
	UserInteractionAnimeService userInteractionAnimeService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
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

	@GetMapping("/")
	public String userTimeline(Model model, Pageable pageable) {
		User user = getLoggedUser();
		
		model.addAttribute("loggedUser", user);
		
		Long userLoggedId = user.getId();
		
		model.addAttribute("userLoggedId", userLoggedId);

		model.addAttribute("user", user);

		String avatar = null;

		if (user.getAvatar() != null) {
			avatar = "data:" + user.getAvatarType() + ";base64,"
					+ new String(Base64.getEncoder().encode(user.getAvatar()));
		}

		model.addAttribute("avatar", avatar);
		
		List<User> users = userRelationshipService.findFollowedByFollower(user);
		
		users.add(user);
		
		List<UserPost> posts = userPostService.findByUserInAndStatusOrderByIdDesc(users, Status.A, pageable);
		
		model.addAttribute("posts", posts);
		model.addAttribute("registeredAnimesQuantity", animeService.findByUser(user).size());
		model.addAttribute("followerQuantity", userRelationshipService.findByFollower(user).size());
		model.addAttribute("followedQuantity", userRelationshipService.findByFollowed(user).size());
		model.addAttribute("interactedAnimes", userInteractionAnimeService.findByUserAndStatus(user, Status.A).size());

		return "timeline";
	}

	@GetMapping("/minhas-informacoes")
	public String userInformations(User user, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		user = userService.findByUsername(userDetails.getUsername());

		model.addAttribute("user", user);

		return "my-informations";
	}

	@PostMapping("/minhas-informacoes")
	public String UpdateUserInformations(@Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return this.userInformations(user, model);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User loggedUser = userService.findByUsername(userDetails.getUsername());

		userService.updateUser(loggedUser.getId(), user.getName(), user.getUsername(), user.getEmail(),
				user.getBirthDate(), user.getAbout());

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
			UserDetails userDetails = (UserDetails) auth.getPrincipal();

			User user = userService.findByUsername(userDetails.getUsername());

			userService.changeAvatar(user.getId(), avatar.getBytes(), avatar.getContentType());

			model.addAttribute("success", "Sua foto foi alterada com sucesso!");
		} catch (IOException e) {
			model.addAttribute("error", "Ocorreu um erro, não foi possível fazer o upload.");
		}

		return this.updateUserAvatar(model);
	}

	@GetMapping("/alterar-senha")
	public String changePasswordScreen(Model model) {
		return "change-password";
	}

	@PostMapping("/alterar-senha")
	public String changePasswordSave(@RequestParam("password") String password,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("newPasswordConfirmation") String newPasswordConfirmation, Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			model.addAttribute("error", "Senha incorreta.");
			return this.changePasswordScreen(model);
		}

		if (newPassword.length() < 8) {
			model.addAttribute("error", "Sua senha deve conter pelo menos 8 caracteres.");
			return this.changePasswordScreen(model);
		}

		if (!newPassword.equals(newPasswordConfirmation)) {
			model.addAttribute("error", "A nova senha não é equivalente à sua confirmação.");
			return this.changePasswordScreen(model);
		}

		userService.changePassword(user.getId(), newPassword);
		model.addAttribute("success", "Sua senha foi alterada com sucesso!");

		return this.changePasswordScreen(model);
	}
	
	@GetMapping("/meu-perfil")
	public String viewProfile(Model model, Pageable pageable) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		Long userLoggedId = user.getId();
		
		model.addAttribute("userLoggedId", userLoggedId);

		model.addAttribute("user", user);

		String avatar = null;

		if (user.getAvatar() != null) {
			avatar = "data:" + user.getAvatarType() + ";base64,"
					+ new String(Base64.getEncoder().encode(user.getAvatar()));
		}

		model.addAttribute("avatar", avatar);
		
		List<User> users = userRelationshipService.findFollowedByFollower(user);
		
		users.add(user);
		
		List<UserPost> posts = userPostService.findByUserInAndStatusOrderByIdDesc(users, Status.A, pageable);
		
		model.addAttribute("posts", posts);
		model.addAttribute("registeredAnimesQuantity", animeService.findByUser(user).size());
		model.addAttribute("followerQuantity", userRelationshipService.findByFollower(user).size());
		model.addAttribute("followedQuantity", userRelationshipService.findByFollowed(user).size());

		return "timeline";
	}
	
	@GetMapping("/usuario/{username}")
	public String viewUser(@PathVariable String username, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User userLogged = userService.findByUsername(userDetails.getUsername());
		Long userLoggedId = userLogged.getId();
		
		model.addAttribute("userLoggedId", userLoggedId);
		
		User user = userService.findByUsername(username);
		
		if ( user == null ) {
			model.addAttribute("errorMessage", "Usuário não encontrado.");
			
			return "error-message";
		}

		model.addAttribute("user", user);

		String avatar = null;

		if (user.getAvatar() != null) {
			avatar = "data:" + user.getAvatarType() + ";base64,"
					+ new String(Base64.getEncoder().encode(user.getAvatar()));
		}

		model.addAttribute("avatar", avatar);
		
		List<UserPost> posts = userPostService.findByUserAndStatus(user, Status.A);
		
		UserRelationship relation = userRelationshipService.findByFollowerAndFollowedAndStatus(userLogged, user, Status.A);
		
		if (relation == null) {
			model.addAttribute("follow", false);
		} else {
			model.addAttribute("follow", true);
		}
		
		model.addAttribute("posts", posts);
		model.addAttribute("registeredAnimesQuantity", animeService.findByUser(user).size());
		model.addAttribute("followerQuantity", userRelationshipService.findByFollower(user).size());
		model.addAttribute("followedQuantity", userRelationshipService.findByFollowed(user).size());
		
		return "timeline";
	}
	
	@GetMapping("/usuario/{username}/{action}")
	public String followOrUnfollow(@PathVariable String username, @PathVariable String action, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User userLogged = userService.findByUsername(userDetails.getUsername());
		Long userLoggedId = userLogged.getId();
		
		model.addAttribute("userLoggedId", userLoggedId);
		
		User user = userService.findByUsername(username);
		
		if ( user == null ) {
			model.addAttribute("errorMessage", "Usuário não encontrado.");
			
			return "error-message";
		}
		
		UserRelationship relation = userRelationshipService.findByFollowerAndFollowed(userLogged, user);
		
		if (action.equals("follow")) {
			if ( relation == null ) {
				relation = new UserRelationship();
				
				relation.setFollower(userLogged);
				relation.setFollowed(user);
				
				userRelationshipService.saveAndFlush(relation);
			} else {
				userRelationshipService.changeStatus(relation.getId(), Status.A);
			}
		} else if (action.equals("unfollow")) {
			userRelationshipService.changeStatus(relation.getId(), Status.I);
		}
		
		return "redirect:/usuario/" + username;
	}
	
	@GetMapping("/usuario/{username}/seguidos")
	public String viewFollowed(@PathVariable String username, Model model) {
		User user = userService.findByUsername(username);
		
		if ( user == null ) {
			model.addAttribute("errorMessage", "Usuário não encontrado.");
			
			return "error-message";
		}
		
		List<UserRelationship> followed = userRelationshipService.findByFollowerAndStatus(user, Status.A);
		
		model.addAttribute("followed", followed);
		
		model.addAttribute("size", followed.size());
		
		return "followed";
	}
	
	@GetMapping("/usuario/{username}/seguidores")
	public String viewFollower(@PathVariable String username, Model model) {
		User user = userService.findByUsername(username);
		
		if ( user == null ) {
			model.addAttribute("errorMessage", "Usuário não encontrado.");
			
			return "error-message";
		}
		
		List<UserRelationship> followers = userRelationshipService.findByFollowedAndStatus(user, Status.A);
		
		model.addAttribute("followers", followers);
		
		model.addAttribute("size", followers.size());
		
		return "followers";
	}
	
	@GetMapping("/usuario/{username}/cadastros")
	public String viewRegisteredAnimes(@PathVariable String username, Model model) {
		User user = userService.findByUsername(username);
		
		if ( user == null ) {
			model.addAttribute("errorMessage", "Usuário não encontrado.");
			
			return "error-message";
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User userLogged = userService.findByUsername(userDetails.getUsername());
		
		List<Anime> animes = animeService.findByUser(user);
		
		model.addAttribute("animes", animes);
		
		model.addAttribute("size", animes.size());
		
		model.addAttribute("user", userLogged);
		
		return "registered-animes";
	}
	
	@GetMapping("/usuario/{username}/interagidos")
	public String viewInteractedAnimes(@PathVariable String username, Model model) {
		User user = userService.findByUsername(username);
		
		if ( user == null ) {
			model.addAttribute("errorMessage", "Usuário não encontrado.");
			
			return "error-message";
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User userLogged = userService.findByUsername(userDetails.getUsername());
		
		List<UserInteractionAnime> animes = userInteractionAnimeService.findByUserAndStatus(user, Status.A);
		
		model.addAttribute("interacted", animes);
		
		model.addAttribute("size", animes.size());
		
		model.addAttribute("user", userLogged);
		
		return "interected-animes";
	}
}
