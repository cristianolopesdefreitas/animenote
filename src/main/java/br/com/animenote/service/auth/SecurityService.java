package br.com.animenote.service.auth;

public interface SecurityService {
	String findLoggedInUsername();
	
	void autologin(String username, String password);
}
