package ch.ese.team6.Service;

import ch.ese.team6.Model.User;

public interface UserService {
	
	void save(User user);
	
	User findByUsername(String username);

}
