package ch.ese.team6.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import antlr.collections.List;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Delivery;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.DeliveryRepository;
import ch.ese.team6.Repository.ItemRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Service.RouteService;

@Controller
@RequestMapping("/route")
public class RouteController{
		
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DeliveryRepository deliveryRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private RouteService routeService;
	
	@GetMapping(path="/add")
	public String createForm(Model model, @RequestParam Date date) {
		model.addAttribute("routeTemplate", new Route(date));
		model.addAttribute("routeDate", date);
		model.addAttribute("trucks", truckRepository.findAll());
		model.addAttribute("drivers", userRepository.findAll());
	        return "route/create";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewRoute (@RequestParam Date routeDate, 
			@RequestParam int driverId, @RequestParam int truck) {
		Route newRoute = new Route(Calendar.getInstance());
		newRoute.setRouteDate(routeDate);
		newRoute.setDeliveryId((long)10);
		newRoute.setDriver(userRepository.findOne((long)driverId));
		newRoute.setTruck(truckRepository.findOne((long)truck));
		routeRepository.save(newRoute);
		return new ModelAndView("/route/profile", "route", newRoute);
	}
	/*
	 * This function creates testdata
	 */
	@GetMapping(path="/addtest")
	public String addSampleRoutes() {
		for (long i= 1;i<4; i++) {
			Route route = new Route(Calendar.getInstance());
			route.setDriver(userRepository.findOne((long)i));
			route.setTruck(truckRepository.findOne((long)i));
			route.setDeliveryId(i); 
			routeRepository.save(route);
		}
		return "redirect:/";
	}
	/*
	@GetMapping(path="/test")
	public String testListFuncions(Model model) {
		long number = 7;
		model.addAttribute(routeRepository.findByDriver(userRepository.findOne(number)));
		return "route/test";
	}
	*/
	@RequestMapping(path="/")
	public String showAllRoutes(Model model) {
		model.addAttribute("routes", routeRepository.findAll());
		return "route/index";
	}
	
	
	@GetMapping(path = "/{routeId}")
	public String showRoute(Model route,@PathVariable Long routeId) {
		route.addAttribute("route", routeRepository.findOne(routeId));
		return "route/profile"; 
	}
	
	@GetMapping(path = "/{routeId}/edit")
	public String editRoute(Model route, @PathVariable long routeId) {
		route.addAttribute("route", routeRepository.findOne(routeId));
		return "route/edit";
	}
	
	@GetMapping(path = "/{routeId}/add")
	public String addDelivery(Model model, @PathVariable long routeId) {
		model.addAttribute("route", routeRepository.findOne(routeId));
		model.addAttribute("delivery", new Delivery());
		return "route/deliveries";
	}
	
	@PostMapping( path = "/{routeId}/add")
	public ModelAndView saveAddedDelivery(@ModelAttribute Delivery delivery, @PathVariable long routeId) {
		Route route = routeRepository.findOne(routeId);
		deliveryRepository.save(delivery);
		route.addDelivery(delivery);
		routeRepository.save(route);
		return new ModelAndView("route/profile", "route", route);
	}
	
	@GetMapping(path = "/delivery/{deliveryId}")
	public String editDelivery(Model model, @PathVariable long deliveryId) {
		model.addAttribute("delivery", deliveryRepository.findOne(deliveryId));
		model.addAttribute("items", itemRepository.findAll());
		return "/route/item";
	}
	
	@PostMapping(path= "/delivery/{deliveryId}")
	public String addItem(Model model, @PathVariable long deliveryId, @RequestParam long itemId) {
		Delivery delivery = deliveryRepository.findOne(deliveryId);
		delivery.addItem(itemRepository.findOne(itemId));
		deliveryRepository.save(delivery);
		model.addAttribute("delivery", deliveryRepository.findOne(deliveryId));
		return "redirect:/route/";
	}
	@PostMapping(path = "/{routeId}/edit")
	public ModelAndView editRoute (@ModelAttribute Route routevalue, @PathVariable long routeId) {
		Route route = routeRepository.findOne(routeId);
		//route.setVehicleId(routevalue.getVehicleId());route.setDriverId(routevalue.getDriverId());
		routeRepository.save(route);
		return new ModelAndView("/route/profile", "route", route);
	}
	
	@DeleteMapping(path = "/{routeId}/edit")
	public void deleteRoute(@PathVariable long routeId, HttpServletResponse response) {
		routeRepository.delete(routeId);
		try {
			response.sendRedirect("/route/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@GetMapping(path="/add/o/{orderId}")
	public String createRouteFromOrder(Model model, @PathVariable long orderId, 
			@RequestParam Date date) {
		model.addAttribute("routeTemplate", new Route(date));
		model.addAttribute("routeDate", date);
		model.addAttribute("trucks", truckRepository.findAll());
		model.addAttribute("drivers", userRepository.findAll());
		model.addAttribute("order", orderRepository.findOne(orderId));
	        return "route/createWithOrder";
		
	}
		
	@PostMapping(path="/add/o/{orderId}")
	public ModelAndView addNewRouteFromOrder (@RequestParam Date routeDate, 
			@RequestParam int driverId, @RequestParam int truck, @PathVariable long orderId) {
		Route route = new Route(Calendar.getInstance());
		route.setRouteDate(routeDate);
		route.setDeliveryId((long)10);
		route.setDriver(userRepository.findOne((long)driverId));
		route.setTruck(truckRepository.findOne((long)truck));
		Order order = orderRepository.findOne(orderId);
		route.addDelivery(new Delivery(order));
		order.scheduleOrder();
		deliveryRepository.save(route.getDeliveries());
		routeRepository.save(route);
		orderRepository.save(order);
		return new ModelAndView("/route/profile", "route", route);
	}
	
	@RequestMapping(path="/onmap")
	public String showOnMap(Model model) {
		Address address = new Address("Meiefeldstrasse 34", "3400 Burgdorf");
		Address address2 = new Address("Giacomettistrasse 114", "7000 Chur");
		String[] addresses = new String[2];
		addresses[0] = address.toString();
		addresses[1] = address2.toString();
		model.addAttribute("address", address);
		model.addAttribute("addresses", addresses);
		return "route/onmap";
	}
	
	@RequestMapping(path="/onmap/{routeid}")
	public String showRouteOnMap(Model model, @PathVariable Long routeid) {
		model.addAttribute("route", routeRepository.findOne(routeid));
		Address address = new Address("Meiefeldstrasse 34", "3400 Burgdorf");
		Address address2 = new Address("Giacomettistrasse 114", "7000 Chur");
		String[] addresses = new String[2];
		addresses[0] = address.toString();
		addresses[1] = address2.toString();
		model.addAttribute("address", address);
		model.addAttribute("addresses", addresses);
		return "route/onmap";
	}
}
