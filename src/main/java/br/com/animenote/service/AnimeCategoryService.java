package br.com.animenote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.AnimeCategory;
import br.com.animenote.repository.AnimeCategoryRepository;

@Service
public class AnimeCategoryService {
	@Autowired
	private AnimeCategoryRepository animeCategoryRepository;
	
	public List<AnimeCategory> findAll() {
		return animeCategoryRepository.findAll();
	}
}
