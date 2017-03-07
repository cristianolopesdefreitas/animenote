package br.com.animenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.model.UserMessage;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

}
