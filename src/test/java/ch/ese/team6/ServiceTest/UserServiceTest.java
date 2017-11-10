package ch.ese.team6.ServiceTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.ese.team6.Service.UserServiceImpl;

public class UserServiceTest {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Before
	public void setup() {
		userService = new UserServiceImpl();

	}
	@Test
	public void generateUsernameWithLongFirstName() {
		String [] userData = {"Manuel","Schacher"};
		String username = userService.generateUsername(userData);
		assertEquals("manuels", username);
	}
	
	@Test
	public void generateUsernameWithShortFirstName() {
		String [] userData = "Ivan,Mann,031 843 72 24,mann@example.com".split(",");
		String username = userService.generateUsername(userData);
		assertEquals("ivanma", username);
	}
	@Test
	public void generateUsernameWithShortNames() {
		String [] userData = {"Lyu","Ho"};
		String username = userService.generateUsername(userData);
		assertEquals("lyuho1", username);
	}

}
