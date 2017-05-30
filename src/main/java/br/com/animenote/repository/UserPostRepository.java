package br.com.animenote.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;
import br.com.animenote.model.UserPost;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
	
	List<UserPost> findByUserInAndStatusOrderByIdDesc(List<User> users, Status status, Pageable pageable);
	
	List<UserPost> findByUserAndStatus(User user, Status status);
	
	@Query(value = "SELECT p FROM UserPost p WHERE p.status = 'A'")
	List<UserPost> findAssociatedPosts();
	
	@Query(value = "SELECT p FROM UserPost p WHERE p.status = ?1")
	List<UserPost> findAssociatedPosts(Status status);
	
	@Modifying
	@Transactional
	@Query("UPDATE UserPost AS p SET p.status = 'I' WHERE p.id = :id")
	int disablePost(@Param("id") Long id);
}
