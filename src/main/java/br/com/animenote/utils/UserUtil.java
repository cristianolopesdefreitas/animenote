package br.com.animenote.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.animenote.model.User;
import br.com.animenote.service.UserService;

public class UserUtil {
	@Autowired
	private UserService userService;
	
	public User getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		return userService.findByUsername(userDetails.getUsername());
	}
}
