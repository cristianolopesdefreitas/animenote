package br.com.animenote.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    
    @Autowired
    private JavaMailSender javaMailService;
	
	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findByName("USER")));
		
		sendMail(user);
		
		return userRepository.saveAndFlush(user);
	}
	
	public User saveAdministrator(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));
		
		sendMail(user);
		
		return userRepository.saveAndFlush(user);
	}
	
	public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
	
	private void sendMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Cadastro Realizado com sucesso!");
        mailMessage.setText("Olá " + user.getName());
        
        javaMailService.send(mailMessage);
	}
	
}
