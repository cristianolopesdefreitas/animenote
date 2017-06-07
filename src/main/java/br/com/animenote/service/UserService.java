package br.com.animenote.service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

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
    private JavaMailSender javaMailSender;
	
	@Autowired
    private SpringTemplateEngine templateEngine;
	
	@Value("${spring.mail.username}")
    private String appEmail;
	
	@Value("${host}")
    private String host;
	
	private static final Logger LOGGER = Logger.getLogger(UserService.class);

	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findByName("USER")));
		
		try {
			sendAccountConfirmationEmail(user);
		} catch (MessagingException e) {
			LOGGER.error("Não foi possível enviar o e-mail.");
		}

		return userRepository.saveAndFlush(user);
	}

	public User saveAdministrator(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));

		try {
			sendAccountConfirmationEmail(user);
		} catch (MessagingException e) {
			LOGGER.error("Não foi possível enviar o e-mail.");
		}

		return userRepository.saveAndFlush(user);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	private void sendAccountConfirmationEmail(User user) throws MessagingException {
//		String confirmationMessage = "Olá, "+ user.getName() +"!<br />Agora basta confirmar seu cadastro!<br /><a href='http://animenote.com.br/"
//				+ user.getUsername() + "/'>Confirme seu cadastro.</a>";
//		
//		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		mailMessage.setTo(user.getEmail());
//		mailMessage.setSubject("Confirmação de Conta");
//		mailMessage.setText(confirmationMessage);
//
//		javaMailSender.send(mailMessage);
		
		final Context context = new Context();
		context.setVariable("name", user.getName());
		context.setVariable("username", user.getUsername());
		context.setVariable("host", this.host);
		
		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
		mimeMessageHelper.setSubject("Confirmação de Conta");
		mimeMessageHelper.setTo(user.getEmail());
		
		try {
			mimeMessageHelper.setFrom(new InternetAddress(appEmail, "Anime Note"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Encode não suportado.");
		}
		
		final String htmlContent = this.templateEngine.process("account-confirmation-email", context);
		mimeMessageHelper.setText(htmlContent, true);
		
//		Inserir imagem
//		try {
//			mimeMessageHelper.addInline("logo.jpg", new FileSystemResource("imgs/logo-pro.jpg"), "image/jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		
		try {
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			LOGGER.error("Não foi possível enviar o e-mail.");
		}
	}
	
	public void sendForgotPasswordEmail(User user, String password) throws MessagingException {
		final Context context = new Context();
		context.setVariable("name", user.getName());
		context.setVariable("username", user.getUsername());
		context.setVariable("password", password);
		context.setVariable("host", this.host);
		
		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
		mimeMessageHelper.setSubject("Recuperação de senha");
		mimeMessageHelper.setTo(user.getEmail());
		
		try {
			mimeMessageHelper.setFrom(new InternetAddress(appEmail, "Anime Note"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Encode não suportado.");
		}
		
		final String htmlContent = this.templateEngine.process("forgot-password-email", context);
		mimeMessageHelper.setText(htmlContent, true);
		
		try {
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			LOGGER.error("Não foi possível enviar o e-mail.");
		}
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
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public List<User> findByNameContaining(String name) {
		return userRepository.findByNameContaining(name);
	}

}
