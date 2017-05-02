package br.com.animenote.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserPost;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
	UserPost findById(Long id);
	
	List<UserPost> findAll();
	
	@Query("SELECT p FROM tb_user_post p WHERE p.status = 'A'")
	List<UserPost> findAssociatedPosts(@Param("id") Long id);
	
	@Modifying
	@Transactional
	@Query("Update tb_user_post AS p SET p.status = 'I' WHERE p.id = :id")
	int disablePost(@Param("id") Long id);
}
