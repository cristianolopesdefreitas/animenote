package br.com.test.animenote.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.animenote.model.Anime;
import br.com.animenote.repository.AnimeRepository;
import br.com.test.animenote.AbstractTest;

public class AnimeRepositoryTest extends AbstractTest {
	private static final Logger LOGGER = Logger.getLogger(AnimeRepositoryTest.class);
	
	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@Transactional
	public void testSearch() {
		List<Anime> animes = animeRepository.search("dr");
		
		LOGGER.info(animes);
	}
}
