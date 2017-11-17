package ch.ese.team6.Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Service.UserService;

@Controller
@RequestMapping("/user")
public class UserBaseController {
		
	@Autowired 	private UserRepository userRepository;
	@Autowired	private UserService userService;
	@Autowired	private RoleRepository roleRepository;
	
	@RequestMapping(path = "/")
	public String showAllUsers(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "user/index";
	}
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("user", new User());
		  model.addAttribute("roles", roleRepository.findAll());
	        return "user/create";
	}

	@PostMapping(path="/add")
	public String addNewUser (Model model, @ModelAttribute("user") @Valid User user,  BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				model.addAttribute("user", user);
				model.addAttribute("roles", roleRepository.findAll());
				return "user/create";
			}
			user.checkPasswords();
			user.checkValidity();
			if(!user.isValid()) throw new BadSizeException("Invalid user.");
			userService.save(user);
		} catch (BadSizeException | DupplicateEntryException e) {
			model.addAttribute("message", e.getMessage());
			model.addAttribute("user", user);
			model.addAttribute("roles", roleRepository.findAll());
			e.printStackTrace();
			return "user/create";
		}
		model.addAttribute("user", user);
		return "user/profile";
	}	
	
	@GetMapping(path = "/{userId}")
	public String showUser(Model user,@PathVariable Long userId) {
		user.addAttribute("user", userRepository.findOne(userId));
		return "user/profile"; 
	}
	
	@GetMapping(path = "/{userId}/edit")
	public String editUser(Model user, @PathVariable long userId) {
		user.addAttribute("user", userRepository.findOne(userId));
		user.addAttribute("statusArray", DataStatus.values());
		user.addAttribute("roles", roleRepository.findAll());
		return "user/edit";
	}
	
	@PostMapping(path = "/{userId}/edit")
	public ModelAndView editUser (@ModelAttribute User uservalue, @PathVariable long userId) {
		User user = userRepository.findOne(userId);
		try {
		user.setFirstname(uservalue.getFirstname());
		user.setSurname(uservalue.getSurname());
		user.setPassword(uservalue.getPassword());
		user.setStatus(uservalue.getStatus());
		user.setRoles(uservalue.getRoles());
		userService.save(user);
		}
		catch(Exception e) {}
		return new ModelAndView("user/profile", "user", user);
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
