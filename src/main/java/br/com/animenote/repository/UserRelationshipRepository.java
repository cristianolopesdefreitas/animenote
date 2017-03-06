package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserRelationship;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {

}
