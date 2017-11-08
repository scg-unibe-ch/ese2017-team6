package ch.ese.team6.Controller;
 
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
 
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Model.User;
 
 @Controller
 @RequestMapping("/driverview")
 public class DriverController {
 	
 	@Autowired
 	private UserRepository userRepository;
 	@Autowired
 	private RouteRepository routeRepository;
 	@Autowired
 	private RoleRepository roleRepository;
 	
 	// Drivers have userrole 1
 	private int userrole = 1;
 	
 	
 	@RequestMapping(path="/")
 	public String showAllDrivers(Model model) {
 		model.addAttribute("drivers", roleRepository.findByName("Driver"));
 		return "driverview/index";
 	}
 	
	@GetMapping(path = "/{userId}")
 	public String showUser(Model driver,@PathVariable Long userId) {
	driver.addAttribute("driver", userRepository.findOne(userId));
	driver.addAttribute("routes", routeRepository.findByDriverId(userId));
		return "driverview/profile"; 
	}
	

}
