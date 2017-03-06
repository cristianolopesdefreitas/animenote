package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.AnimeCategory;

@Repository
public interface AnimeCategoryRepository extends JpaRepository<AnimeCategory, Long> {

}
