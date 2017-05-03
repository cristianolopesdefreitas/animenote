package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserPost;
import br.com.animenote.model.UserPostComment;

@Repository
public interface UserPostCommentRepository extends JpaRepository<UserPostComment, Long> {
	List<UserPostComment> findByUserPost(UserPost userPost);
}
