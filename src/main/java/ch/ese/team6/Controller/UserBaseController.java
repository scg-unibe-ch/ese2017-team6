package ch.ese.team6.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserBaseController {
		
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping
	public String showAllUsers(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "user/index";
	}
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("users", new User());
	        return "user/create";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewUser (@ModelAttribute User uservalue) {
		User user = new User();
		user=uservalue;
		userRepository.save(user);
		return new ModelAndView("/user/profile", "user", user);
	}	
	
	@GetMapping(path = "/{userId}")
	public String showUser(Model user,@PathVariable Long userId) {
		user.addAttribute("user", userRepository.findOne(userId));
		return "user/profile"; 
	}
	
	@GetMapping(path = "/{userId}/edit")
	public String editUser(Model user, @PathVariable long userId) {
		user.addAttribute("user", userRepository.findOne(userId));
		return "user/edit";
	}
	
	@PostMapping(path = "/{userId}/edit")
	public ModelAndView editUser (@ModelAttribute User uservalue, @PathVariable long userId) {
		User user = userRepository.findOne(userId);
		user.setFirstname(uservalue.getFirstname());user.setSurname(uservalue.getSurname());
		user.setPassword(uservalue.getPassword());
		userRepository.save(user);
		return new ModelAndView("/user/profile", "user", user);
	}
	
	@DeleteMapping(path = "/{userId}/edit")
	public void deleteUser(@PathVariable long userId, HttpServletResponse response) {
		userRepository.delete(userId);
		try {
			response.sendRedirect("/user/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
