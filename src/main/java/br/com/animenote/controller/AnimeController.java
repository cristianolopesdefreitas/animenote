package br.com.animenote.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.animenote.model.Anime;
import br.com.animenote.service.AnimeService;

@Controller
public class AnimeController {
	@Autowired
	private AnimeService service;
	
	@GetMapping("/animes")
	public String findAll(Model model) {
		model.addAttribute("animes", service.findAll());
		
		return "animes";
	}
	
	@GetMapping("/add")
    public String add(Anime anime, Model model) {        
        model.addAttribute("anime", anime);
         
        return "register";
    }
	
	@PostMapping("/save/anime")
	public String save(@Valid Anime anime, BindingResult result, Model model) {
		if(result.hasErrors()) {
            return this.add(anime, model);
        }
         
        service.save(anime);
         
        return "redirect:/animes";
	}
}
