package br.com.animenote.service;

import org.springframework.stereotype.Service;

import br.com.animenote.model.Anime;
import br.com.animenote.repository.AnimeRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AnimeService {
	@Autowired
	private AnimeRepository repository;

	public List<Anime> findAll() {
		return repository.findAll();
	}

	public Anime findOne(Long id) {
		return repository.findOne(id);
	}

	public Anime save(Anime anime) {
		return repository.saveAndFlush(anime);
	}

	public void delete(Long id) {
		repository.delete(id);
	}
}
