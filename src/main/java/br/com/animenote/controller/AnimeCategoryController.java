package br.com.animenote.controller;

import java.util.Iterator;

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

import br.com.animenote.model.AnimeCategory;
import br.com.animenote.model.Role;
import br.com.animenote.model.User;
import br.com.animenote.service.AnimeCategoryService;
import br.com.animenote.service.UserService;

@Controller
public class AnimeCategoryController {
	@Autowired
	AnimeCategoryService animeCategoryService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/administracao/categorias")
	public String viewCategories(AnimeCategory animeCategory, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("categories", animeCategoryService.findAll());
		model.addAttribute("animeCategory", animeCategory);
		
		return "categories";
	}
	
	@GetMapping("/administracao/categorias/{id}")
	public String editCategory(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("categories", animeCategoryService.findAll());
		
		AnimeCategory category = animeCategoryService.findOne( id );
		
		if (category == null) {
			model.addAttribute("animeCategory", new AnimeCategory());
		} else {
			model.addAttribute("animeCategory", category);
		}
		
		return "categories";
	}
	
	@PostMapping("/administracao/categorias")
	public String saveCategory(AnimeCategory animeCategory, BindingResult result, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (result.hasErrors()) {
			return this.viewCategories(animeCategory, model);
		}
		
		animeCategoryService.saveAndFlush( animeCategory );
		
		return "redirect:/administracao/categorias";
	}
	
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
}
