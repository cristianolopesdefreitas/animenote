package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserPostComment;

@Repository
public interface UserPostCommentRepository extends JpaRepository<UserPostComment, Long> {

}
