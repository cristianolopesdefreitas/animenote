package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserPost;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {

}
