package br.com.animenote.service;

import java.util.HashSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.UserPost;
import br.com.animenote.repository.UserPostRepository;

@Service
public class UserPostService {
	@Autowired
	UserPostRepository userPostRepository;
	
	private static final Logger LOGGER = Logger.getLogger(UserPostService.class);
	
	public UserPost savePost(UserPost userPost) {
		LOGGER.info("Salvando postagem");
		return userPostRepository.saveAndFlush(userPost);
	}
	
	public UserPost findById(Long id) {
		LOGGER.info("Buscando postagem pelo id");
		return userPostRepository.findById(id);
	}
	
	public HashSet<UserPost> findAssociatedPosts(Long id) {
		LOGGER.info("Buscando postagens para a timeline");
		return userPostRepository.findAssociatedPosts(id);
	}
}
