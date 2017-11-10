package ch.ese.team6.ModelTest;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.Role;
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
		String username = "ivanma";
		try {
			user.setUsername(username);
		}
		catch(BadSizeException e){
			fail("Correct username should not throw exception." + e.getMessage());
		}
	}
	@Test
	public void usernameIsLongEnought() {
		User user = new User();
		String username = "012345";
		try {
			user.setUsername(username);
		}
		catch(BadSizeException e){
			fail("Correct username should not throw exception." + e.getMessage());
		}
	}
	@Test
	public void usernameSmallEnough() {
		User user = new User();
		String username = "01234567890123456789012345678901";
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
	@Test
	public void inValidUserMissingEntries() {
		try {
			User user = new User();
			user.setUsername("example");
			user.setPassword("password");
			user.setPasswordConfirm("password");
			user.setEmail("info@example.com");
			assertFalse(user.isValid());
		}
		catch(BadSizeException e) {
			e.printStackTrace();
			fail("Assigning values should not throw exception");
		}
		
	}
	
	@Test
	public void inValidUserUnmatchingPasswords() {
		try {
			User user = new User();
			user.setUsername("userqed");
			user.setPassword("password");
			user.setPasswordConfirm("foobarbaz");
			user.setFirstname("Alexander");
			user.setSurname("Seeerq");
			user.setEmail("info@example.com");
			user.setPhoneNumber("079 888 44 22");
			assertFalse(user.isValid());
			}
		catch(BadSizeException e){
			e.printStackTrace();
			fail("Assigning values should not throw exception");
		}
	}
	
	@Test
	public void ValidUser() {
		try {
			User user = new User();
			user.setUsername("userqed");
			user.setPassword("password");
			user.setPasswordConfirm("password");
			user.setFirstname("Alexander");
			user.setSurname("Seeerq");
			user.setEmail("info@example.com");
			user.setPhoneNumber("079 888 44 22");
			Role role = new Role();
			role.setName("TESTER");
			user.setRole(role);
			assertTrue(user.isValid());
			}
		catch(BadSizeException e){
			e.printStackTrace();
			fail("Assigning values should not throw exception");
		}
	}
}
