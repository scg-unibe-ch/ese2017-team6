package ch.ese.team6.ServiceTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.User;
import ch.ese.team6.Service.SampleDataServiceImpl;
import ch.ese.team6.Service.UserServiceImpl;

public class SampleDataServiceTest {
	
	@Autowired private UserServiceImpl userService;
	@Autowired private SampleDataServiceImpl sampleDataService;
	
	@Before
	public void setUp() {
		userService = new UserServiceImpl();
		sampleDataService = new SampleDataServiceImpl();
	}
	
	@Test
	public void csvParsing() {
		String csv = "a,b,c;d, e      ,f";
		String[][] data = sampleDataService.parseCsv(csv);
		for(String[] line : data) {
			for(String value : line) {
			}
		}
		assertEquals("a", data[0][0]);
		assertEquals("b", data[0][1]);
		assertEquals("c", data[0][2]);
		assertEquals("d", data[1][0]);
		assertEquals("e", data[1][1]);
		assertEquals("f", data[1][2]);
	}
	
	@Test
	public void userImportgenerateUser() {
		String [][] userdata = sampleDataService.parseCsv(userCsv);
		int i = 0;
		User[] userArray = new User[userdata.length];
		for(String []user : userdata) {
			userArray[i] = new User();
			try {
				userArray[i].setUsername(userService.generateUsername(user));
				userArray[i].setFirstname(user[0]);
				userArray[i].setSurname(user[1]);
				userArray[i].setPhoneNumber(user[2]);
				userArray[i].setEmail(user[3]);
				i++;
			} catch (BadSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Import should succeed.");
			}
		}	
		assertEquals("ivanma", userArray[0].getUsername());
	}
	
	
	private String userCsv = 
			"Ivan,Mann,031 843 72 24,mann@example.com;" + 
			"Lee,Fisher,031 843 72 25,fisher@example.com;" + 
			"Shannon,Guzman,031 843 72 26,guzman@example.com;";

}
