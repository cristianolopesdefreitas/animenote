package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
	Anime findById(Long id);
	
	List<Anime> findByUser(User user);
}
