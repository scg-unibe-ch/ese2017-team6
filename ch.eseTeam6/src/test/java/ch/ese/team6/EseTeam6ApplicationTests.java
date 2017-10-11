package ch.ese.team6;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EseTeam6ApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Autowired
	public void UsersShouldBeSuccessToSave() {
		long count = userRepository.count();
		Users user = new Users("username","anzeigename", "password" );
		userRepository.save(user);
		Assert.assertTrue(userRepository.count()+1 == count);
	}

}
