package br.com.animenote.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;
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
		return userPostRepository.findOne(id);
	}
	
	public List<UserPost> findAssociatedPosts() {
		LOGGER.info("Buscando postagens para a timeline");
		return userPostRepository.findAssociatedPosts();
	}
	
	public List<UserPost> findAll() {
		LOGGER.info("Buscando todas as postagens para a timeline");
		return userPostRepository.findAll();
	}
	
	public int disablePost(Long id) {
		LOGGER.info("Desabilitanto postagem");
		return userPostRepository.disablePost(id);
	}
	
	public List<UserPost> findByUserInAndStatusOrderByIdDesc(List<User> users, Status status, Pageable pageable) {
		LOGGER.info("Buscando postagens da timeline");
		return userPostRepository.findByUserInAndStatusOrderByIdDesc(users, status, pageable);
	}
	
	public List<UserPost> findByUserAndStatus(User user, Status status) {
		LOGGER.info("Buscando postagens do usuário");
		return userPostRepository.findByUserAndStatus(user, status);
	}
}
