package ch.ese.team6.Service;

import java.util.Date;
import java.util.List;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.User;

public interface UserService {
	
	void save(User user) throws BadSizeException, DupplicateEntryException;
	
	User findByUsername(String username);
	boolean existByUsername(String username);
	List<User>findFreeDrivers(Date date);
	List<User>findFreeDrivers(Date date,IDelivarable o);
	
	
	String generateUsername(String[] userData);
	

}
