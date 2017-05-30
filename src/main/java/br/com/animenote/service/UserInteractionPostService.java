package br.com.animenote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.constants.Status;
import br.com.animenote.constants.UserPostInteractions;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionPost;
import br.com.animenote.model.UserPost;
import br.com.animenote.repository.UserInteractionPostRepository;

@Service
public class UserInteractionPostService {
	@Autowired
	private UserInteractionPostRepository userInteractionPostRepository;
	
	public List<UserInteractionPost> findByStatus(Status status) {
		return userInteractionPostRepository.findByStatus(status);
	}
	
	public UserInteractionPost findByPostAndUser(UserPost userPost, User user) {
		return userInteractionPostRepository.findByPostAndUser(userPost, user);
	}
	
	public UserInteractionPost saveAndFlush(UserInteractionPost userInterationPost) {
		return userInteractionPostRepository.saveAndFlush(userInterationPost);
	}
	
	public List<UserInteractionPost> findByStatusAndUserPostInteractions(Status status, UserPostInteractions userPostInteractions) {
		return userInteractionPostRepository.findByStatusAndUserPostInteractions(status, userPostInteractions);
	}
}