package ch.ese.team6.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RouteRepository routeRepository;

    @Override
    public void save(User user) throws BadSizeException {
    	if (user.isValid()) {
    		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    		userRepository.save(user);
    	}
    	else throw new BadSizeException("These values are not correct, user won't be saved!");
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public List<User> findFreeUsers(String date){
		List<Route> routes = routeRepository.findByRouteDate(date);
		List<User> occupiedUsers = new ArrayList<User>();
		for(Route route: routes) {
			occupiedUsers.add(route.getDriver());
		}
		List<User>freeUsers = userRepository.findAll();
		freeUsers.removeAll(occupiedUsers);
		return freeUsers;
	}
    
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