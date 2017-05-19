package br.com.test.animenote.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.animenote.model.UserRelationship;
import br.com.animenote.repository.UserRelationshipRepository;
import br.com.test.animenote.AbstractTest;

public class UserRelationshipRepositoryTest extends AbstractTest {
	
	@Autowired
	private UserRelationshipRepository userRelationshipRepository;
	
	private static final Logger LOGGER = Logger.getLogger(UserRelationshipRepositoryTest.class);
	
	@Test
	public void findAll() {
		List<UserRelationship> userRelationships = this.userRelationshipRepository.findAll();
		
		LOGGER.info(userRelationships);
	}
}
