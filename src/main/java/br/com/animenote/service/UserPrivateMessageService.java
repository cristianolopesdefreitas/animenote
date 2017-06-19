package br.com.animenote.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.constants.Status;
import br.com.animenote.model.User;
import br.com.animenote.model.UserPrivateMessage;
import br.com.animenote.repository.UserPrivateMessageRepository;

@Service
public class UserPrivateMessageService {
	@Autowired
	UserPrivateMessageRepository userPrivateMessageRepository;
	
	public UserPrivateMessage saveAndFlush(UserPrivateMessage userPrivateMessage) {
		return userPrivateMessageRepository.saveAndFlush(userPrivateMessage);
	}
	
	public List<UserPrivateMessage> findByUserReceiverAndStatusOrderByIdDesc(User userReceiver, Status status) {
		return userPrivateMessageRepository.findByUserReceiverAndStatusOrderByIdDesc(userReceiver, status);
	}
	
	public List<UserPrivateMessage> findByUserSenderAndStatusOrderByIdDesc(User userSender, Status status) {
		return userPrivateMessageRepository.findByUserSenderAndStatusOrderByIdDesc(userSender, status);
	}
	
	public UserPrivateMessage findByUserReceiverAndId(User userReceiver, Long id) {
		return userPrivateMessageRepository.findByUserReceiverAndId(userReceiver, id);
	}
	
	public UserPrivateMessage findByUserSenderAndId(User userSender, Long id) {
		return userPrivateMessageRepository.findByUserSenderAndId(userSender, id);
	}
}
