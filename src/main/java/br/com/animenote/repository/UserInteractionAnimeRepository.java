package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.constants.Status;
import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionAnime;

@Repository
public interface UserInteractionAnimeRepository extends JpaRepository<UserInteractionAnime, Long> {
	UserInteractionAnime findByUserAndAnimeAndStatus(User user, Anime anime, Status status);
	
	UserInteractionAnime findByUserAndAnime(User user, Anime anime);
	
	List<UserInteractionAnime> findByUser(User user);
	
	List<UserInteractionAnime> findByUserAndStatus(User user, Status status);
}
