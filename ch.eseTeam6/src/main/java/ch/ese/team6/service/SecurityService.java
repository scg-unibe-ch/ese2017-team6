package ch.ese.team6.service;

import ch.ese.team6.models.users.Users;

public interface SecurityService {

	String findLoggedInUsername();
	
	void autologin(String username, String password);

}
