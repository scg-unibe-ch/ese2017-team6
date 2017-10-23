package ch.ese.team6.Security;
/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;


@Service
public class UserSecurityService {

	@Autowired
	private UserDetailsManager userDetailsManager;
	
	public boolean canCreate() {
		return true;
	}
	
	public boolean canRead(String name) {
		return getAuthenticatedUser().getUsername() == name;
	}
	
	public boolean canUpdate(String name) {
		return getAuthenticatedUser().getUsername() == name;
	}
	
	public boolean canDelete(String name) {
		return getAuthenticatedUser().getUsername() == name;
	}

	public UserDetails getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userDetailsManager.loadUserByUsername(authentication.getName());
	}

}
*/