package br.com.test.animenote.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;
import br.com.animenote.model.UserPost;
import br.com.animenote.repository.UserPostRepository;
import br.com.animenote.repository.UserRelationshipRepository;
import br.com.animenote.repository.UserRepository;
import br.com.test.animenote.AbstractTest;

public class UserPostRepositoryTest extends AbstractTest {
	
	private static final Logger LOGGER = Logger.getLogger(UserPostRepositoryTest.class);
	
	@Autowired
	private UserPostRepository userPostRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRelationshipRepository userRelationshipRepository;
	
	@Test
	@Transactional
	public void testFindAll() {
		List<UserPost> userPosts = this.userPostRepository.findAll();
		
		LOGGER.info(userPosts);
	}
	
	@Test
	@Transactional
	public void testFindAssociatedPosts() {
		List<UserPost> posts = this.userPostRepository.findAssociatedPosts(Status.I);
		
		LOGGER.info(posts);
	}
	
	@Test
	@Transactional
	public void testFindAssociatedPosts2() {
		List<UserPost> posts = this.userPostRepository.findAssociatedPosts();
		
		LOGGER.info(posts);
	}
	
	@Test
	@Transactional
	public void testFindPosts() {
		User user = userRepository.findOne(1L);
		
		List<User> users = userRelationshipRepository.findFollowedByFollower(user);
		
		users.add(user);
		
		List<UserPost> posts = userPostRepository.findByUserInAndStatus(users, Status.A);
		
		LOGGER.info(posts);
	}

}
