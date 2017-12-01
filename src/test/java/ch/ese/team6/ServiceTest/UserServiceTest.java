package ch.ese.team6.ServiceTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.ese.team6.Application;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {
	
	@Autowired	private UserService userService;
	@Autowired	private RoleRepository roleRepository;
	@Autowired	private UserRepository userRepository;
	
	private Role role;
	
	@Before
	public void setup() {
		role = new Role();
		role.setName("TESTER");
		roleRepository.save(role);
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
	@Test
	public void validUserShouldBeSaved() {
		int numberofentries = userRepository.findAll().size();
		User savedUser1 = new User();
		try {
			savedUser1.setUsername("testuser");
			savedUser1.setPassword("password");
			savedUser1.setPasswordConfirm("password");
			savedUser1.setPhoneNumber("0344447788");
			savedUser1.setFirstname("Firstname");
			savedUser1.setSurname("Surname");
			savedUser1.setEmail("example@example.com");
			savedUser1.setRole(role);
			assertTrue(savedUser1.isValid());
			userService.save(savedUser1);
			if (!(numberofentries + 1 == userRepository.findAll().size())) {
			fail("User wasnt saved.");
			}
		} catch (BadSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Entries should be valid.");
		} catch (DupplicateEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("User should be saved.");
		}
		assertTrue(userService.existByUsername("testuser"));
		User user = userService.findByUsername("testuser");
		assertEquals("testuser", user.getUsername());
		assertEquals("Firstname", user.getFirstname());
		assertEquals("Surname", user.getSurname());
		assertEquals("example@example.com", user.getEmail());
		
	}
	
	@Test
	public void shouldRejectDuplicateUser() {
		User savedUser = new User();
		try {
			savedUser.setEmail("example@example.com");
			savedUser.setUsername("0123456");
			savedUser.setPassword("password");
			savedUser.setPasswordConfirm("password");
			savedUser.setPhoneNumber("0344447788");
			savedUser.setFirstname("Firstname");
			savedUser.setSurname("Surname");
			savedUser.setRole(role);
			assertTrue(savedUser.isValid());
			userService.save(savedUser);
		} catch (BadSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Entries should be valid.");
		} catch (DupplicateEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("User should be saved.");
		}
		User user2 = new User();
		try {
			user2.setEmail("example@example.com");
			user2.setUsername("0123456");
			user2.setPassword("password");
			user2.setPasswordConfirm("password");
			user2.setPhoneNumber("0344447788");
			user2.setFirstname("Firstname");
			user2.setSurname("Surname");
			user2.setRole(role);
			assertTrue(user2.isValid());
			userService.save(user2);
			fail("User should be rejected");
		} catch (BadSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Entries should be valid.");
		} catch (DupplicateEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
