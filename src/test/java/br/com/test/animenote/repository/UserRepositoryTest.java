package br.com.test.animenote.repository;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.animenote.model.User;
import br.com.animenote.repository.UserRepository;
import br.com.test.animenote.AbstractTest;
import br.com.test.animenote.ApplicationTestConfig;

public class UserRepositoryTest extends AbstractTest {
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger LOGGER = Logger.getLogger(UserRepositoryTest.class);
	
	@Test
	public void findByUsername() {
		User user = userRepository.findByUsername("cristianolopesdefreitas");
		
		if (user != null) {
			LOGGER.info("Usuário encontrado");
		} else {
			LOGGER.error("Usuário não encontrado");
		}
	}
}
