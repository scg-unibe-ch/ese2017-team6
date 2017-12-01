package ch.ese.team6.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Service.RouteService;
import ch.ese.team6.Service.TruckService;
import ch.ese.team6.Service.UserService;

@Controller
@RequestMapping (path= "/logistics")
public class LogisticController {
	
	@Autowired OrderRepository orderRepository;
	@Autowired RouteRepository routeRepository;
	@Autowired RouteService routeService;
	@Autowired TruckService truckService;
	@Autowired UserService userService;
	
	@GetMapping
	public String cockpit(Model model) {
		Calendar today = Calendar.getInstance();
		List<Order> allOrders = orderRepository.findAll();
		List<Order> urgentOrders = new ArrayList<Order>();
		List<Order> nonUrgentOrders = new ArrayList<Order>();
		for (Order order: allOrders) {
			if(order.isOpen() && order.getDeliveryDate().before(today.getTime())) {
				urgentOrders.add(order);
			}
			else if(order.isOpen()) {
				nonUrgentOrders.add(order);
			}
		}
		List<Route> allRoutes= routeRepository.findAll();
		List<Route> startedRoutes = new ArrayList<Route>();
		List<Route> scheduledRoutes = new ArrayList<Route>();
		for(Route route : allRoutes) {
			if(route.isRouteStarted()) {
				startedRoutes.add(route);
			}
			else if(!route.isRouteFinished()) {
				scheduledRoutes.add(route);
			}
		}
		
		model.addAttribute("urgentOrders", urgentOrders);
		model.addAttribute("nonUrgentOrders", nonUrgentOrders);
		model.addAttribute("startedRoutes", startedRoutes);
		model.addAttribute("scheduledRoutes", scheduledRoutes);
		return "logistics/cockpit";
	}
	
}
