package br.com.animenote.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;
import br.com.animenote.model.UserRelationship;
import br.com.animenote.repository.UserRelationshipRepository;

@Service
public class UserRelationshipService {
	@Autowired
	UserRelationshipRepository userRelationshipRepository;
	
	private static final Logger LOGGER = Logger.getLogger(UserPostService.class);
	
	public List<User> findByFollower(User follower) {
		LOGGER.info("Buscando seguidores de");
		return userRelationshipRepository.findFollowedByFollower(follower);
	}
	
	public List<User> findByFollowed(User followed) {
		LOGGER.info("Buscando seguidos por");
		return userRelationshipRepository.findFollowerByFollowed(followed);
	}
	
	public List<User> findFollowedByFollower(User user) {
		LOGGER.info("Buscando seguidos");
		return userRelationshipRepository.findFollowedByFollower(user);
	}
	
	public UserRelationship findByFollowerAndFollowedAndStatus(User follower, User followed, Status status) {
		LOGGER.info("Buscando relação");
		return userRelationshipRepository.findByFollowerAndFollowedAndStatus(follower, followed, status);
	}
	
	public UserRelationship findByFollowerAndFollowed(User follower, User followed) {
		LOGGER.info("Buscando relação");
		return userRelationshipRepository.findByFollowerAndFollowed(follower, followed);
	}
	
	public UserRelationship findRelation(User follower, User followed) {
		LOGGER.info("Buscando relação");
		return userRelationshipRepository.findRelation(follower, followed);
	}
	
	public UserRelationship saveAndFlush(UserRelationship userRelationship) {
		LOGGER.info("Criando uma nova relação");
		return userRelationshipRepository.saveAndFlush(userRelationship);
	}
	
	public int changeStatus(Long id, Status status) {
		LOGGER.info("Alterando o status de um relacionamento");
		return userRelationshipRepository.changeStatus(id, status); 
	}
	
	public List<User> findByFollowerAndStatus(User follower, Status status) {
		LOGGER.info("Buscando seguidores");
		return userRelationshipRepository.findByFollowerAndStatus(follower, status);
	}
	
	public List<User> findByFollowedAndStatus(User followed, Status status) {
		LOGGER.info("Buscando seguidos");
		return userRelationshipRepository.findByFollowedAndStatus(followed, status);
	}
}
