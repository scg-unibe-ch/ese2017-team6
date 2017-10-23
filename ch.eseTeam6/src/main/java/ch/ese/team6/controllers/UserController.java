package ch.ese.team6.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;
import ch.ese.team6.service.SecurityService;
import ch.ese.team6.service.UserService;
import ch.ese.team6.validator.UserValidator;

@Controller
@RequestMapping("/user")
public class UserController {
		
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserValidator userValidator;
	
	@RequestMapping(value ="/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("userForm", new Users());
		return "/users/registration";
	}
	
	@RequestMapping(value= "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") Users userForm, BindingResult bindingResult, Model model) {
		userValidator.validate(userForm, bindingResult);
		if (bindingResult.hasErrors()) {
			return "users/registration";
		}
		userService.save(userForm);
		securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
		return "redirect:/hello";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "login";
	    }
	
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
	public String showUser(Model user,@PathVariable Long userId) {
		user.addAttribute("user", userRepository.findOne(userId));
		return "user/profile"; 
	}
	
	@GetMapping(path = "/{userId}/edit")
	public String editUser(Model user, @PathVariable long userId) {
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
