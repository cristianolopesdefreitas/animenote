package br.com.animenote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.animenote.model.User;
import br.com.animenote.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repository;
	
	public User saveAndFlush(User user) {
		return repository.saveAndFlush(user);
	}
}
