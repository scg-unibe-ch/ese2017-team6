package ch.ese.team6.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.UsersOld;

@RestController
@RequestMapping("/user")
public class UserControllerOld {
	
	@Autowired
	private UserRepository userRepository;
	
	private int currentId = 1;
	
	@RequestMapping("/")
	public ModelAndView showallUsers() {
		//Get all Users
		return new ModelAndView("user/userIndex");
		
	}
	@GetMapping("/create")
	public ModelAndView createForm() {
		
		return new ModelAndView("user/userCreate");
	}
	/*
	@PostMapping("/create")
	public ModelAndView createUser(@RequestParam String userName, String realName, String password)  {
		UsersOld user = new UsersOld(currentId ,userName, realName, password);
		userRepository.save(user);
		return new ModelAndView("/profile");
	}
*/
}
