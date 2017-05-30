package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.constants.Status;
import br.com.animenote.constants.UserPostInteractions;
import br.com.animenote.model.User;
import br.com.animenote.model.UserInteractionPost;
import br.com.animenote.model.UserPost;

@Repository
public interface UserInteractionPostRepository extends JpaRepository<UserInteractionPost, Long> {
	List<UserInteractionPost> findByStatus(Status status);
	
	UserInteractionPost findByPostAndUser(UserPost userPost, User user);
	
	List<UserInteractionPost> findByStatusAndUserPostInteractions(Status status, UserPostInteractions userPostInteractions);
}
