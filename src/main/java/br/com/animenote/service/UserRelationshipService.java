package br.com.animenote.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.User;
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
}
