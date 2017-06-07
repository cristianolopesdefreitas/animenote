package br.com.animenote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.service.AnimeService;
import br.com.animenote.service.UserService;

@Controller
public class DefaultController {
	@Autowired
	private UserService userService;
	
	@Autowired
	AnimeService animeService;
	
	@GetMapping("/busca")
	public String search(String filter, String name, Model model) {
		if ( name.equals("") ) {
			model.addAttribute("errorMessage", "Digite um termo para a pesquisa.");
			
			return "error-message";
		}
		
		model.addAttribute("name", name);
		
		if ( filter.equals("anime") ) {
			List<Anime> animes = animeService.findByNameContaining(name);
			model.addAttribute("animes", animes);
			
			return "search-anime";
		} else if ( filter.equals("user") ) {
			List<User> users = userService.findByNameContaining(name);
			model.addAttribute("users", users);
			
			return "search-user";
		} else {
			model.addAttribute("errorMessage", "Filtro Inválido.");
			
			return "error-message";
		}
	}
}
