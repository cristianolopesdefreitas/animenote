package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.User;
import br.com.animenote.model.UserRelationship;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
	
	@Query("SELECT u.followed FROM UserRelationship u WHERE u.follower = ?1")
	List<User> findFollowedByFollower(User follower);
	
	@Query("SELECT u.follower FROM UserRelationship u WHERE u.followed = ?1")
	List<User> findFollowerByFollowed(User followed);
}
