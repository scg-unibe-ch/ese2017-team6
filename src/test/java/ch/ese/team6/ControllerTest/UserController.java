package ch.ese.team6.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import ch.ese.team6.Application;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserController {
	
	@Autowired MockMvc mockMvc;

	@Test
	public void shouldShowUserIndex() {
		try {
			this.mockMvc.perform(get("/user/")).andExpect(status().is3xxRedirection());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
