package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.Anime;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

}
