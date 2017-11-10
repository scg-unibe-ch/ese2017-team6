package ch.ese.team6.Service;

import java.util.List;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.User;

public interface UserService {
	
	void save(User user) throws BadSizeException, DupplicateEntryException;
	
	User findByUsername(String username);
	boolean existByUsername(String username);
	List<User>findFreeUsers(String date);
	
	String generateUsername(String[] userData);
	

}
