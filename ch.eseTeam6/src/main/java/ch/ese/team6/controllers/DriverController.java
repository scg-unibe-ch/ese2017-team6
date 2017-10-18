package ch.ese.team6.controllers;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.ese.team6.models.routes.RouteRepository;
import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

@Controller
@RequestMapping("/driverview")
public class DriverController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RouteRepository routeRepository;
	
	// Drivers have userrole 1
	private int userrole = 1;
	
	
	@RequestMapping(path="/")
	public String showAllDrivers(Model model) {
		model.addAttribute("drivers", userRepository.findByUserrole(userrole));
		return "driverview/driver";
	}
	
	@GetMapping(path = "/{userId}")
	public String showUser(Model driver,@PathVariable Long userId) {
		driver.addAttribute("driver", userRepository.findOne(userId));
		driver.addAttribute("routes", routeRepository.findByDriver(userRepository.findOne(userId)));
		return "driverview/profile"; 
	}

}
