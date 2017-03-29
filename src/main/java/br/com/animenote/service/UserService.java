package br.com.animenote.service;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.animenote.constants.Status;
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
		String confirmationMessage = "Olá, "+ user.getName() +"!<br />Agora basta confirmar seu cadastro!<br /><a href='http://animenote.com.br/"
				+ user.getUsername() + "/'>Confirme seu cadastro.</a>";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Confirmação de Conta");
		mailMessage.setText(confirmationMessage);

		//javaMailService.send(mailMessage);
	}
	
	public boolean confirmAccount(String username) {
		User user = userRepository.findByUsername(username);

		if (user != null) {
			user.setStatus(Status.A);
			userRepository.changeStatus(user.getId(), Status.A);
			
			return true;
		}
		
		return false;
	}
	
	public void changeAvatar(Long id, byte[] avatar, String avatarType) {
		userRepository.changeAvatar(id, avatar, avatarType);
	}
	
	public void updateUser(Long id, String name, String username, String email, Calendar birthDate, String about) {
		userRepository.updateUser(id, name, username, email, birthDate, about);
	}
	
	public void changePassword(Long id, String password) {
		userRepository.changePassword(id, bCryptPasswordEncoder.encode(password));
	}

}
