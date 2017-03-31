package br.com.animenote.repository;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.animenote.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
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
