package br.com.animenote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.AnimeCreator;
import br.com.animenote.repository.AnimeCreatorRepository;

@Service
public class AnimeCreatorService {
	@Autowired
	private AnimeCreatorRepository animeCreatorRepository;
	
	public List<AnimeCreator> findAll() {
		return animeCreatorRepository.findAll();
	}
	
	public AnimeCreator findById(Long id) {
		return animeCreatorRepository.findById(id);
	}
	
	public AnimeCreator findOne(Long id) {
		return animeCreatorRepository.findOne(id);
	}
	
	public AnimeCreator saveAndFlush(AnimeCreator animeCreator) {
		return animeCreatorRepository.saveAndFlush(animeCreator);
	}
}
