package ch.ese.team6.Controller;

import org.hibernate.boot.model.source.spi.Orderable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Service.RouteService;
import ch.ese.team6.Service.TruckService;
import ch.ese.team6.Service.UserService;

@Controller
@RequestMapping (path= "/logistics")
public class LogisticController {
	
	@Autowired OrderRepository orderRepository;
	@Autowired RouteService routeService;
	@Autowired TruckService truckService;
	@Autowired UserService userService;
	
	@GetMapping
	public String cockpit(Model model) {
		
		return "logistics/cockpit";
	}
	
}
