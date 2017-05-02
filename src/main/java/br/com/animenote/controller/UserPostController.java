package br.com.animenote.controller;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;

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

import br.com.animenote.model.User;
import br.com.animenote.model.UserPost;
import br.com.animenote.service.UserPostService;
import br.com.animenote.service.UserService;

@Controller
public class UserPostController {
	@Autowired
	UserPostService userPostService;

	@Autowired
	private UserService userService;

	@GetMapping("/postagem")
	public String writePost(UserPost userPost, Model model) {
		model.addAttribute("userPost", userPost);

		return "post-registration";
	}

	@PostMapping("/postagem")
	public String savePost(@Valid UserPost userPost, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return this.writePost(userPost, model);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());

		userPost.setUser(user);
		userPost.setPostDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")));

		userPostService.savePost(userPost);

		model.addAttribute("success", "Postagem salva com sucesso!");

		return this.writePost(new UserPost(), model);
	}

	@GetMapping("/postagem/{id}")
	public String writePost(@PathVariable Long id, Model model) {
		UserPost userPost = userPostService.findById(id);
		String avatar = null, postDate = null;

		if (userPost != null) {
			if (userPost.getUser().getAvatar() != null) {
				avatar = "data:" + userPost.getUser().getAvatarType() + ";base64,"
						+ new String(Base64.getEncoder().encode(userPost.getUser().getAvatar()));
			}

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm - dd/MM/yyyy");

			postDate = formatter.format(userPost.getPostDate().getTime());

			model.addAttribute("avatar", avatar);
			model.addAttribute("postDate", postDate);
			model.addAttribute("userPost", userPost);
		} else {
			model.addAttribute("message", "Postagem não encontrada.");
		}

		return "post";
	}
	
	@GetMapping("/excluir-postagem/{id}")
	public String deletePost(@PathVariable Long id, Model model) {
		UserPost userPost = userPostService.findById(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		
		if (user.getId() == userPost.getUser().getId()) {
			userPostService.disablePost(userPost.getId());
			model.addAttribute("message", "Postagem apagada com sucesso!");
		} else {
			model.addAttribute("message", "Não é possível apagar esta postagem.");
		}
		
		return "deleted-post";
	}
	
	@GetMapping("/editar-postagem/{id}")
	public String editPost(@PathVariable Long id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userService.findByUsername(userDetails.getUsername());
		UserPost userPost = userPostService.findById(id);
		
		if (userPost.getUser().getId() == user.getId()) {
			model.addAttribute("userPost", userPost);
		} else {
			model.addAttribute("userPost", new UserPost());
		}
		
		return "post-registration";
	}
}
