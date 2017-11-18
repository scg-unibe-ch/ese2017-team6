package ch.ese.team6.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.ese.team6.Exception.InconsistentOrderStateException;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Service.RouteService;

@Controller
@RequestMapping(path = "/schedule")
public class SchedulingController {
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private RouteService routeService;
	
	@RequestMapping
	public String overview(Model model) {
		model.addAttribute("orders", orderRepository.findAll());
		model.addAttribute("routes", routeService.selectWithLeftCapacity());
		return "schedule/overview";
	}
	
	@GetMapping(path ="/{orderId}")
	public String addOrderToRoute (Model model, @PathVariable long orderId) {
		Order order = orderRepository.findOne(orderId);
		model.addAttribute("order", order);
		
		List<Route> routes = routeRepository.findAll();
		// Elimate routes where the order does not fit
		for(int i = routes.size()-1;i>=0;i--) {
			Route r = routes.get(i);
			if(!r.doesIDelivarableFit(order)) {
				routes.remove(r);
			}
		}
		
		model.addAttribute("routes", routes);
		return "schedule/addOrder";
	}
	
	@PostMapping(path ="/{orderId}")
	public String createDelivery(Model model, @PathVariable long orderId, @RequestParam long routeId) {
		Route route = routeRepository.findOne(routeId);
		Order order = orderRepository.findOne(orderId);
	
		if (route.isFull()) return"redirect:/schedule/"+orderId;
		
		route.addDelivarable(order);
		try {
			order.schedule();
		} catch (InconsistentOrderStateException e) {
			e.printStackTrace();
		}
		
		routeRepository.save(route);
		orderRepository.save(order);
		model.addAttribute("orders", orderRepository.findAll());
		model.addAttribute("routes", routeRepository.findAll());
		return "schedule/overview";
	}
}
