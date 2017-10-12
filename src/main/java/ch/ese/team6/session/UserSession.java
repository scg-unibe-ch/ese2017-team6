package ch.ese.team6.session;

import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

public class UserSession {
	private long userId;
	private long timestamp;
	private UserRepository userRepository;
	
	
	public UserSession(long userId) {
		this.userId = userId;
		this.getNewTimestamp();
	}
	
	public int getUserrole() {
		Users user = userRepository.findOne((long)userId);
		return user.getUserrole();
	}
	public boolean timeOut() {
		if(this.timestamp + 1000000 < System.currentTimeMillis()) return true;
		return false;
	}
	public void getNewTimestamp() {
		this.timestamp = System.currentTimeMillis();
		
	}
}
