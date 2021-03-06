package ch.ese.team6.Service;


import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.UserRepository;
/**
 * UserServiceImpl provides different services for the controllers. It's mostly used to 
 * check for free Drivers or to save a new user. 
 * @author Domi
 *
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired  private UserRepository userRepository;
    @Autowired  private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired	private AddressRepository addressRepository;
    @Autowired 	private RoleRepository roleRepository;
	/**
	 * checks if the user is valid, encrypts his password and saves him.
	 */
    @Override
    public void save(User user) throws BadSizeException, DupplicateEntryException {
    	if (user.isValid()) {
    		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    	}
    	else throw new BadSizeException("These values are not correct, user won't be saved!");
    	if ((user.hasId()) || this.existByUsername(user.getUsername())) {
    		throw new DupplicateEntryException("username already exists.");
    	}
    	userRepository.save(user);
    }
    /**
     * checks if the user exists.
     */
    @Override
	public boolean existByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
    /**
     * finds the user in the database and returns the user.
     */
	@Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
	
    /**
     * returns all unscheduled drivers for the choosen date
     */
    @Override
	public List<User> findFreeDrivers(Date date){
    	Set<Role> roles = roleRepository.findByName("DRIVER");
		List<User> freeDrivers = userRepository.findByRoles(roles);
		for(int i =freeDrivers.size()-1; i>=0; i--) {
			User user = freeDrivers.get(i);
			if(user.isOccupied(date)) {
				freeDrivers.remove(user);
			}
		}
		return freeDrivers;
    }
    
    /**
	 * Will return the trucks free at date data and with enough capacity to transport o
	 * and with enough time to transport o
	 */
	@Override
	public List<User> findFreeDrivers(Date date, IDelivarable o) {
		List<User> freeUsers = this.findFreeDrivers(date);
		for(int i = freeUsers.size()-1; i>=0; i--) {
			User user = freeUsers.get(i);
			
			// Check if the truck has enough time to drive to the deliveryAddres
		    Date nextTimeThisUserIsOccupied = user.nextAppointment(date);
				if(nextTimeThisUserIsOccupied!=null) {
				    Address deposit = addressRepository.findOne(OurCompany.depositId);
				    //drive to the address and back
				    long requiredTime = 2*deposit.getDistance(o.getAddress())*60l*1000l;
				    Date userWouldFinish = CalendarService.computeTaskEnd(CalendarService.getWorkingStart(date), requiredTime);
				    if(nextTimeThisUserIsOccupied.before(userWouldFinish)) {
				    	freeUsers.remove(i);
				    }
				}
			
			
		}
		return freeUsers;
	}

    
    
    /**
     * generates a username from userdata. Used for the sampleData
     */
    @Override
	public String generateUsername(String[] userData) {
		String username = userData[0].trim().toLowerCase() + 
				userData[1].substring(0, 1).trim().toLowerCase();
		if(username.length() < 6 ) {
			if(userData[1].length() + username.length() > 6) {
				username = username.concat(userData[1].substring(1, 7-username.length()));
			}
			else {
				username = userData[0].trim().toLowerCase() + userData[1].trim().toLowerCase();
				while (username.length() < 6) {
					int i = 1;
					username = username +i;
				}
			}
		}
		return username;
    }
}