package ch.ese.team6.Service;

import java.util.List;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.User;

public interface UserService {
	
	void save(User user) throws BadSizeException;
	
	User findByUsername(String username);
	
	List<User>findFreeUsers(String date);

}
