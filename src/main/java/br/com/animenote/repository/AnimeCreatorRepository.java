package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.AnimeCreator;

@Repository
public interface AnimeCreatorRepository extends JpaRepository<AnimeCreator, Long> {
	AnimeCreator findById(Long id);
}
