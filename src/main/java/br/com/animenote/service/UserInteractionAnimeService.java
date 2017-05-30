package br.com.animenote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.constants.Status;
import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionAnime;
import br.com.animenote.repository.UserInteractionAnimeRepository;

@Service
public class UserInteractionAnimeService {
	@Autowired
	private UserInteractionAnimeRepository userInteractionAnimeRepository;
	
	public UserInteractionAnime findByUserAndAnimeAndStatus(User user, Anime anime, Status status) {
		return userInteractionAnimeRepository.findByUserAndAnimeAndStatus(user, anime, status);
	}
	
	public UserInteractionAnime findByUserAndAnime(User user, Anime anime) {
		return userInteractionAnimeRepository.findByUserAndAnime(user, anime);
	}
	
	public List<UserInteractionAnime> findByUser(User user) {
		return userInteractionAnimeRepository.findByUser(user);
	}
	
	public List<UserInteractionAnime> findByUserAndStatus(User user, Status status) {
		return userInteractionAnimeRepository.findByUserAndStatus(user, status);
	}
	
	public UserInteractionAnime saveAndFlush(UserInteractionAnime userInteractionAnime) {
		return userInteractionAnimeRepository.saveAndFlush(userInteractionAnime);
	}
}
