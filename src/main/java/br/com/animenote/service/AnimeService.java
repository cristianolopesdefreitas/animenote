package br.com.animenote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.repository.AnimeRepository;

@Service
public class AnimeService {
	@Autowired
	private AnimeRepository animeRepository;

	public List<Anime> findAll() {
		return animeRepository.findAll();
	}

	public Anime findById(Long id) {
		return animeRepository.findById(id);
	}

	public Anime save(Anime anime) {
		return animeRepository.saveAndFlush(anime);
	}

	public void delete(Long id) {
		animeRepository.delete(id);
	}
	
	public List<Anime> findByUser(User user) {
		return animeRepository.findByUser(user);
	}
}
