package br.com.animenote.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.animenote.model.User;
import br.com.animenote.repository.RoleRepository;
import br.com.animenote.repository.UserRepository;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;
	
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveAndFlush(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
		return userRepository.saveAndFlush(user);
	}
	
	public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
