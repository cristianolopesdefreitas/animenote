package br.com.animenote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;
import br.com.animenote.model.UserPrivateMessage;

@Repository
public interface UserPrivateMessageRepository extends JpaRepository<UserPrivateMessage, Long> {
	List<UserPrivateMessage> findByUserReceiverAndStatusOrderByIdDesc(User userReceiver, Status status);
	
	List<UserPrivateMessage> findByUserSenderAndStatusOrderByIdDesc(User userSender, Status status);
	
	UserPrivateMessage findByUserReceiverAndId(User userReceiver, Long id);
	
	UserPrivateMessage findByUserSenderAndId(User userSender, Long id);
}
