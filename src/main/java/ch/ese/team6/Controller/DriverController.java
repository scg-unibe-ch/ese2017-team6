package ch.ese.team6.Controller;
 
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
 
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Model.Role;
 
 @Controller
 @RequestMapping("/driverview")
 public class DriverController {
 	
 	@Autowired
 	private UserRepository userRepository;
 	@Autowired
 	private RouteRepository routeRepository;
 	@Autowired
 	private RoleRepository roleRepository;
 	
 	
 	
 	@RequestMapping(path="/")
 	public String showAllDrivers(Model model) {
 		Set<Role> roles = new HashSet<Role>();
 		roles.addAll(roleRepository.findByName("DRIVER"));
 		model.addAttribute("drivers", userRepository.findByRoles(roles));
 		return "driverview/index";
 	}
 	
	@GetMapping(path = "/{userId}")
 	public String showUser(Model driver,@PathVariable Long userId) {
	driver.addAttribute("driver", userRepository.findOne(userId));
	driver.addAttribute("routes", routeRepository.findByDriverId(userId));
		return "driverview/profile"; 
	}
	

}
