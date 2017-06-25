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

import br.com.animenote.model.AnimeCreator;
import br.com.animenote.model.Role;
import br.com.animenote.model.User;
import br.com.animenote.service.AnimeCreatorService;
import br.com.animenote.service.UserService;

@Controller
public class AnimeCreatorController {
	@Autowired
	AnimeCreatorService animeCreatorService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/administracao/criadores")
	public String viewCreators(AnimeCreator animeCreator, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("creators", animeCreatorService.findAll());
		model.addAttribute("animeCreator", animeCreator);
		
		return "creators";
	}
	
	@GetMapping("/administracao/criadores/{id}")
	public String editCreator(@PathVariable Long id, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		model.addAttribute("creators", animeCreatorService.findAll());
		
		AnimeCreator creator = animeCreatorService.findOne( id );
		
		if (creator == null) {
			model.addAttribute("animeCreator", new AnimeCreator());
		} else {
			model.addAttribute("animeCreator", creator);
		}
		
		return "creators";
	}
	
	@PostMapping("/administracao/criadores")
	public String saveCreator(AnimeCreator animeCreator, BindingResult result, Model model) {
		model.addAttribute("loggedUser", getLoggedUser());
		model.addAttribute("isAdmin", isAdmin());
		
		if (result.hasErrors()) {
			return this.viewCreators(animeCreator, model);
		}
		
		if ( animeCreator.getName().equals("") ) {
			model.addAttribute("error", "O nome do criador é obrigatório.");
			return this.viewCreators(animeCreator, model);
		}
		
		animeCreatorService.saveAndFlush( animeCreator );
		
		return "redirect:/administracao/criadores";
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
