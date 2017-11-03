package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.ese.team6.Model.Delivery;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.DeliveryRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RouteRepository;

@Controller
@RequestMapping(path = "/schedule")
public class SchedulingController {
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	
	@RequestMapping
	public String overview(Model model) {
		model.addAttribute("orders", orderRepository.findAll());
		model.addAttribute("routes", routeRepository.findAll());
		return "schedule/overview";
	}
	
	@GetMapping(path ="/{orderId}")
	public String addOrderToRoute (Model model, @PathVariable long orderId) {
		model.addAttribute("order", orderRepository.findOne(orderId));
		model.addAttribute("routes", routeRepository.findAll());
		return "schedule/addOrder";
	}
	
	@PostMapping(path ="/{orderId}")
	public String createDelivery(Model model, @PathVariable long orderId, @RequestParam long routeId) {
		Route route = routeRepository.findOne(routeId);
		Order order = orderRepository.findOne(orderId);
		route.addDelivery(new Delivery(order));
		deliveryRepository.save(route.getDeliveries());
		routeRepository.save(route);
		model.addAttribute("orders", orderRepository.findAll());
		model.addAttribute("routes", routeRepository.findAll());
		return "schedule/overview";
	}
}