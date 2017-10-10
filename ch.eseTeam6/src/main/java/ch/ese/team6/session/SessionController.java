package ch.ese.team6.session;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;
import ch.ese.team6.security.Login;

public class SessionController {
	
	private UserRepository userRepository;
	
	@GetMapping(path = "/login")
	public String createForm(Model login) {
		  login.addAttribute("session", new Login());
	        return "/login";
	}
	
	@PostMapping(path="/login")
	public void StartSession(@ModelAttribute Login userLogin) throws Exception {
		if (!userRepository.existsByUsername(userLogin.getUsername())) throw new Exception ("User not found");
		Users user = userRepository.findByUsername(userLogin.getUsername());
		if (!(userLogin.getPassword() == user.getPassword())) throw new Exception ("Wrong password!");
		UserSession session = new UserSession(user.getId());
	}
	
}
