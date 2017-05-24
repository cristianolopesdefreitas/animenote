package br.com.animenote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionAnime;
import br.com.animenote.repository.UserInteractionAnimeRepository;

@Service
public class UserInteractionAnimeService {
	@Autowired
	private UserInteractionAnimeRepository userInteractionAnimeRepository;
	
	public UserInteractionAnime findByUserAndAnime(User user, Anime anime) {
		return userInteractionAnimeRepository.findByUserAndAnime(user, anime);
	}
}
