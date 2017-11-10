package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;
import org.junit.Test;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.User;

public class UserModellTest {

	@Test
	public void usernameIsTooSmall() {
		User user = new User();
		String username = "12345     ";
		try {
			user.setUsername(username);
			fail("setUsername should throw exception");
		}
		catch(BadSizeException e) {
			assertEquals(e.getMessage(), "username should be between 6 and 32 chars long!");
		}
		
	}
	@Test
	public void usernameIsTooBig() {
		User user = new User();
		String username = "012345678901234567890123456789012";
		try {
			user.setUsername(username);
			fail("setUsername should throw exception");
		}
		catch(BadSizeException e) {
			assertEquals(e.getMessage(), "username should be between 6 and 32 chars long!");
		}
	}
	
	@Test
	public void usernameIsCorrect() {
		User user = new User();
		String username = "0123456";
		try {
			user.setUsername(username);
		}
		catch(BadSizeException e){
			fail("Correct username should not throw exception." + e.getMessage());
		}
	}
	@Test
	public void firstNameIsEmpty() {
		User user = new User();
		String firstname = "        ";
		try {
			user.setFirstname(firstname);
			fail("setFirstname should throw exception");
		}
		catch(BadSizeException e) {
			assertEquals(e.getMessage(), "firstname can't be empty");
		}
	}
	@Test
	public void firstNameIsCorrect() {
		User user = new User();
		String firstname = "Abc";
		try {
			user.setFirstname(firstname);
			
		}
		catch(BadSizeException e) {
			fail("setFirstname shouldn't throw exception");
		}
	}
	
	@Test
	public void surNameIsEmpty() {
		User user = new User();
		String surname = "        ";
		try {
			user.setSurname(surname);
			fail("setSurname should throw exception");
		}
		catch(BadSizeException e) {
			assertEquals(e.getMessage(), "surname can't be empty");
		}
	}
	@Test
	public void surNameIsCorrect() {
		User user = new User();
		String surname = "DEF";
		try {
			user.setFirstname(surname);
			
		}
		catch(BadSizeException e) {
			fail("setSurname shouldn't throw exception");
		}
	}
	@Test
	public void emailIsEmpty() {
		User user = new User();
		String email = "    ";
		try {
			user.setEmail(email);
			fail("Incorrect Email shoud throw Exception");
		}
		catch(BadSizeException e) {
			assertEquals(e.getMessage(), "Email has not the correct format.");
		}
	}
	
	@Test
	public void emailIsIncorrect() {
		User user = new User();
		String email = "ihqwe@@¦@dad_co.com.com";
		try {
			user.setEmail(email);
			fail("Incorrect Email shoud throw Exception");
		}
		catch(BadSizeException e) {
			assertEquals(e.getMessage(), "Email has not the correct format.");
		}
	}
	
	@Test
	public void emailIsCorrect() {
		User user = new User();
		String email = "info@example.com";
		try {
			user.setEmail(email);
		}
		catch(BadSizeException e) {
			fail("Correct email shoudl not throw exception.");
		}
	}
}
