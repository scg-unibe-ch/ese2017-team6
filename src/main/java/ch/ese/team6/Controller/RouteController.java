package ch.ese.team6.Controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

//import antlr.collections.List;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Delivery;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.OrderItemRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Service.RouteService;
import ch.ese.team6.Service.TruckService;
import ch.ese.team6.Service.UserService;

@Controller
@RequestMapping("/route")
public class RouteController{
		
	@Autowired private RouteRepository routeRepository;
	@Autowired private TruckRepository truckRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private OrderRepository orderRepository;
	@Autowired private AddressRepository addressRepository;
	@Autowired private OrderItemRepository orderitemrepository;
	
	@Autowired private RouteService routeService;
	@Autowired private TruckService truckService;
	@Autowired private UserService userService;
	
	@GetMapping(path="/add")
	public String createForm(Model model, @RequestParam String date) {
		model.addAttribute("routeTemplate", new Route(date));
		model.addAttribute("routeDate", date);
		model.addAttribute("trucks", truckService.findFreeTrucks(date));
		model.addAttribute("drivers", userService.findFreeUsers(date));
	        return "route/create";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewRoute (@RequestParam String routeDate, 
			@RequestParam int driverId, @RequestParam int truck) {
		Route newRoute = new Route();
		newRoute.setRouteDate(routeDate);
		newRoute.setDriver(userRepository.findOne((long)driverId));
		newRoute.setTruck(truckRepository.findOne((long)truck));
		routeRepository.save(newRoute);
		return new ModelAndView("route/profile", "route", newRoute);
	}

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
		
		return "route/deliveries";
	}
	
	@PostMapping( path = "/{routeId}/add")
	public ModelAndView saveAddedDelivery(@ModelAttribute Order order, @PathVariable long routeId) {
		Route route = routeRepository.findOne(routeId);
		route.addDelivarable(order);
		orderRepository.save(order);
		routeRepository.save(route);
		return new ModelAndView("route/profile", "route", route);
	}
	
	@PostMapping(path = "/{routeId}/edit")
	public ModelAndView editRoute (@ModelAttribute Route routevalue, @PathVariable long routeId) {
		Route route = routeRepository.findOne(routeId);
		//route.setVehicleId(routevalue.getVehicleId());route.setDriverId(routevalue.getDriverId());
		routeRepository.save(route);
		return new ModelAndView("route/profile", "route", route);
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
			@RequestParam String date) {
		model.addAttribute("routeTemplate", new Route(date));
		model.addAttribute("routeDate", date);
		model.addAttribute("trucks", truckService.findFreeTrucks(date));
		model.addAttribute("drivers", userService.findFreeUsers(date));
		model.addAttribute("order", orderRepository.findOne(orderId));
	        return "route/createWithOrder";
		
	}
		
	@PostMapping(path="/add/o/{orderId}")
	public ModelAndView addNewRouteFromOrder (@RequestParam String routeDate, 
			@RequestParam int driverId, @RequestParam int truck, @PathVariable long orderId) {
		Route route = new Route();
		route.setRouteDate(routeDate);
		route.setDriver(userRepository.findOne((long)driverId));
		route.setTruck(truckRepository.findOne((long)truck));
		Order order = orderRepository.findOne(orderId);
		route.addDelivarable(order);
		order.scheduleOrder();
		routeRepository.save(route);
		orderRepository.save(order);
		return new ModelAndView("route/profile", "route", route);
	}
	
	@RequestMapping(path="/onmap")
	public String showOnMap(Model model) {
		Address address = new Address("Meiefeldstrasse 34", "3400 Burgdorf");
		Address address2 = new Address("Giacomettistrasse 114", "7000 Chur");
		String[] addresses = new String[2];
		addresses[0] = address.getStreetandCity();
		addresses[1] = address2.getStreetandCity();
		model.addAttribute("address", address);
		model.addAttribute("addresses", addresses);
		return "route/onmap";
	}
	
	@RequestMapping(path="/onmap/{routeid}")
	public String showRouteOnMap(Model model, @PathVariable Long routeid) {
		
		Route route = new Route();
		route = routeRepository.findOne(routeid);
		model.addAttribute("route", route);
		model.addAttribute("deliveries", routeRepository.findOne(routeid).getDeliveries());
		
		String[] addressesfinal = createAddressStringArray(routeid);
		model.addAttribute("addresses", addressesfinal);
		
		return "route/onmap";
	}
	
	
	@PostMapping(value="/acceptDelivery/{addressid}")
    public ModelAndView acceptDelivery(@RequestParam long routeid, @PathVariable Long addressid) {
    
		Address address = addressRepository.findOne(addressid);
		Route route = routeRepository.findOne(routeid);
		
		ArrayList<OrderItem> orderitems = route.getArrayOfOI();
		
		for(OrderItem oi :orderitems)
		{
			if(oi.getAddress().equals(address))
			{
				oi.setOrderItemStatus("delivered");
				route.removeOrderItem(oi);
			}
		}
		
		ModelAndView ret= new ModelAndView("route/onmap");
		ret.addObject("route", route);
		ret.addObject("deliveries", routeRepository.findOne(routeid).getDeliveries());
		
		String[] addressesfinal = createAddressStringArray(routeid);
		ret.addObject("addresses", addressesfinal);
		
		return ret;
       
    }


	/**
	 * creates an Array with addresses stored as Strings
	 * @param routeid
	 * @return
	 */
	private String[] createAddressStringArray(Long routeid) {
		ArrayList<Address> addresses = new ArrayList<Address>();
		
		for(Delivery deliver : routeRepository.findOne(routeid).getDeliveries())
		{
			addresses.add(deliver.getAddress());
		}
		
		String[] addressesfinal = new String[addresses.size()];
		
		for(int i=0; i<addresses.size();i++)
		{
			addressesfinal[i] = addresses.get(i).getStreetandCity();
		}
		return addressesfinal;
	}
}
