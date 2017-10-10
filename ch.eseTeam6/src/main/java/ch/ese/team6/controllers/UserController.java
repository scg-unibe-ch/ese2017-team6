package ch.ese.team6.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

@Controller
@RequestMapping("/user")
public class UserController {
		
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("users", new Users());
	        return "user/createForm";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewUser (@ModelAttribute Users uservalue) {
		Users user = new Users();
		user=uservalue;
		userRepository.save(user);
		return new ModelAndView("/user/profile", "user", user);
	}
	
	@RequestMapping(path="/")
	public String showAllUsers(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "user/userIndex";
	}
	
	
	@GetMapping(path = "/{userId}")
	public String editUser(Model user,@PathVariable Long userId) {
		user.addAttribute("user", userRepository.findOne(userId));
		return "user/profile"; 
	}
	
	@GetMapping(path = "/{userId}/edit")
	public String editUserForm(Model user, @PathVariable long userId) {
		user.addAttribute("user", userRepository.findOne(userId));
		return "user/editForm";
	}
	
	@PostMapping(path = "/{userId}/edit")
	public ModelAndView editUser (@ModelAttribute Users uservalue, @PathVariable long userId) {
		Users user = userRepository.findOne(userId);
		user.setRealName(uservalue.getRealName());user.setPassword(uservalue.getPassword());
		userRepository.save(user);
		return new ModelAndView("/user/profile", "user", user);
	}
	
	
	@GetMapping(path ="/edit")
	public String editUserForm(Model user) {
		
		return "user/editForm";
	}
	
}
