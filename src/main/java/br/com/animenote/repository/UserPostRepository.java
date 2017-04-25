package br.com.animenote.repository;

import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserPost;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
	UserPost findById(Long id);
	
	@Query("SELECT id, post, postDate from tb_user_post as p WHERE p.status = 'A'")
	HashSet<UserPost> findAssociatedPosts(@Param("id") Long id);
}
