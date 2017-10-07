package ch.ese.team6.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@RequestMapping(path="/all")
	public ModelAndView showAllUsers() {
		return new ModelAndView("user/userIndex","User", getAllUsers());
	}
	
	public Iterable<Users> getAllUsers(){
		return userRepository.findAll();
	}
}
