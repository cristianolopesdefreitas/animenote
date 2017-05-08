package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
	Anime findById(Long id);
	
	List<Anime> findByUser(User user);
	
	@Query(value = "SELECT a FROM Anime a WHERE a.status = 'A' AND a.name LIKE %:name%")
	List<Anime> search(String name);
}
