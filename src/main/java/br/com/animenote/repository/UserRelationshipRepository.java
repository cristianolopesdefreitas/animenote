package br.com.animenote.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;
import br.com.animenote.model.UserRelationship;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
	
	@Query("SELECT u.followed FROM UserRelationship u WHERE u.follower = ?1")
	List<User> findFollowedByFollower(User follower);
	
	@Query("SELECT u.follower FROM UserRelationship u WHERE u.followed = ?1")
	List<User> findFollowerByFollowed(User followed);
	
	UserRelationship findByFollowerAndFollowed(User follow, User followed);
	
	UserRelationship findByFollowerAndFollowedAndStatus(User follow, User followed, Status status);
	
	@Query("SELECT u FROM UserRelationship u WHERE u.follower = ?1 AND u.followed = ?2")
	UserRelationship findRelation(User follower, User followed);
	
	@Modifying
	@Transactional
	@Query("UPDATE UserRelationship u SET u.status = :status WHERE u.id = :id")
	int changeStatus(@Param("id") Long id, @Param("status") Status status);
}
