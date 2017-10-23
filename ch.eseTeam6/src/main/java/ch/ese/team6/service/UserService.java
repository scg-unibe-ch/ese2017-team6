package ch.ese.team6.service;

import ch.ese.team6.models.users.Users;

public interface UserService {
	void save(Users user);
	
	Users findByUsername(String username);
}
