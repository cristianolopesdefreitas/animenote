package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.User;
import br.com.animenote.model.UserRelationship;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
	List<User> findByFollower(User follower);
	
	List<User> findByFollowed(User followed);
}
