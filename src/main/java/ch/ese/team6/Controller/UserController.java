package ch.ese.team6.Controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private UserDetailsManager userDetailsManager;

	@PreAuthorize("@userSecurityService.canCreate()")
	@GetMapping(path = "/create")
	public ModelAndView createForm() {
		return new ModelAndView("user/form", "user", new User("user", "", Collections.emptyList()));
	}

	@PreAuthorize("@userSecurityService.canCreate()")
	@PostMapping(path = "")
	public ModelAndView create(@RequestParam String username, @RequestParam String password) {
		// NOTE users need an authority, otherwise they are treated as non-existing
		User user = new User(username, password, Collections.singletonList(new SimpleGrantedAuthority("user")));
		userDetailsManager.createUser(user);
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		return new ModelAndView("redirect:/user/{username}", "username", user.getUsername());
	}

	@PreAuthorize("@userSecurityService.canRead(#username)")
	@GetMapping(path = "/{username}")
	public ModelAndView read(@PathVariable(value = "username") String username) {
		UserDetails user = userDetailsManager.loadUserByUsername(username);
		return new ModelAndView("user/read", "user", user);
	}

	@PreAuthorize("@userSecurityService.canUpdate(#username)")
	@PutMapping(path = "/{username}")
	public ModelAndView update(@PathVariable(value = "username") String username, @RequestParam String password) {
		String oldPassword = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getPassword();
		userDetailsManager.changePassword(oldPassword, password);
		UserDetails user = userDetailsManager.loadUserByUsername(username);
		return new ModelAndView("redirect:/user/{username}", "username", user.getUsername());
	}

	@PreAuthorize("@userSecurityService.canDelete(#username)")
	@DeleteMapping(path = "/{username}")
	public ModelAndView delete(@PathVariable(value = "username") String username) {
		userDetailsManager.deleteUser(username);
		return new ModelAndView("redirect:/");
	}

}
