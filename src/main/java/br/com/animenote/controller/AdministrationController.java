package br.com.animenote.controller;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.animenote.model.Role;
import br.com.animenote.model.User;
import br.com.animenote.service.UserService;

@Controller
public class AdministrationController {
	@Autowired
	private UserService userService;
	
	private User getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		return userService.findByUsername(userDetails.getUsername());
	}
	
	private boolean isAdmin() {
		User user = getLoggedUser();
		
		for (Iterator<Role> iterator = user.getRoles().iterator(); iterator.hasNext();) {
			if ( "ADMIN".equals(iterator.next().getName()) ) {
				return true;
			}
		}
		
		return false;
	}
}
