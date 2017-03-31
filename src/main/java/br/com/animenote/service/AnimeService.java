package br.com.animenote.service;

import org.springframework.stereotype.Service;

import br.com.animenote.model.Anime;
import br.com.animenote.repository.AnimeRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AnimeService {
	@Autowired
	private AnimeRepository animeRepository;

	public List<Anime> findAll() {
		return animeRepository.findAll();
	}

	public Anime findOne(Long id) {
		return animeRepository.findOne(id);
	}

	public Anime save(Anime anime) {
		return animeRepository.saveAndFlush(anime);
	}

	public void delete(Long id) {
		animeRepository.delete(id);
	}
	
	public List<Anime> findByUserId(Long id) {
		return animeRepository.findByUserId(id);
	}
}
