package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.Anime;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionAnime;

@Repository
public interface UserInteractionAnimeRepository extends JpaRepository<UserInteractionAnime, Long> {
	UserInteractionAnime findByUserAndAnime(User user, Anime anime);
}
