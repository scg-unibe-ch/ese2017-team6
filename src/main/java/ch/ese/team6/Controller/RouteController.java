package ch.ese.team6.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

import ch.ese.team6.Exception.InconsistentOrderStateException;
//import antlr.collections.List;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Service.CalendarService;
import ch.ese.team6.Service.OurCompany;
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
	@Autowired private TruckService truckService;
	@Autowired private UserService userService;
	
	@GetMapping(path="/add")
	public String createForm(Model model, @RequestParam String date) {
		
		Date dateD = CalendarService.parseDate(date);
		model.addAttribute("routeTemplate", new Route(dateD,null));
		model.addAttribute("routeDate", dateD);
		model.addAttribute("trucks", truckService.findFreeTrucks(dateD));
		model.addAttribute("drivers", userService.findFreeDrivers(dateD));
		//model.addAttribute("addresses", addressRepository.findAll());
	        return "route/create";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewRoute (@RequestParam long routeDate, 
			@RequestParam int driverId, @RequestParam int truck) {
		
		Date routeDateD = new Date(routeDate); 
		Route newRoute = new Route(routeDateD,addressRepository.findOne(OurCompany.depositId));
		newRoute.setRouteStartDate(routeDateD);
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

		Date dateD = CalendarService.parseDate(date);
		model.addAttribute("routeTemplate", new Route(dateD,addressRepository.findOne(OurCompany.depositId)));
		model.addAttribute("routeDate", date);
		Order order =  orderRepository.findOne(orderId);
		//only the trucks with enough capacity
		model.addAttribute("trucks", truckService.findFreeTrucks(dateD,order));
		model.addAttribute("drivers", userService.findFreeUsers(dateD,order));
		model.addAttribute("order", order);
		//model.addAttribute("addresses", addressRepository.findAll());
	        return "route/createWithOrder";
		
	}
		
	@PostMapping(path="/add/o/{orderId}")
	public ModelAndView addNewRouteFromOrder (@RequestParam String routeDate, 
			@RequestParam int driverId, @RequestParam int truck, @PathVariable long orderId) {
		

		Date routeDateD = CalendarService.parseDate(routeDate);

		Route route = new Route(routeDateD,addressRepository.findOne(OurCompany.depositId));
		route.setDriver(userRepository.findOne((long)driverId));
		route.setTruck(truckRepository.findOne((long)truck));
		Order order = orderRepository.findOne(orderId);
		
		//the order may not fit into the truck
		if (!route.doesIDelivarableFit(order)) return new ModelAndView("redirect:/schedule/");
		
		
		route.addDelivarable(order);
		try {
			order.schedule();
		} catch (InconsistentOrderStateException e) {
			e.printStackTrace();
		}
		routeRepository.save(route);
		orderRepository.save(order);
		return new ModelAndView("route/profile", "route", route);
	}
	
	
	@RequestMapping(path="/onmap/{routeid}")
	public String showRouteOnMap(Model model, @PathVariable Long routeid) {
		
		Route route = routeRepository.findOne(routeid);
		model.addAttribute("route", route);
		
		Address origin = route.getDeposit();
		Address destination = route.getCurrentAddress();

		// Origin Address of Route
		model.addAttribute("deposit", route.getDeposit());
		
		// All the deliveries (open or not)
		model.addAttribute("alldeliveries", route.getAllDeliveries());
		// When the route gets started, the last address is the deposit address of the route
		model.addAttribute("lastAddress", route.getDeposit());
		model.addAttribute("deliveries", route.getOpenDeliveries());
		
		String[] addressesfinal = createAddressStringArray(routeid, true, false);
		model.addAttribute("addresses", addressesfinal);
		
		model.addAttribute("duration", origin.getDistanceStr(destination));
		
		return "route/onmap";
	}
	
	
	@PostMapping(value="/acceptDelivery/{addressid}")
    public ModelAndView acceptDelivery(@RequestParam long routeid, @PathVariable Long addressid) {
    
		Address address = addressRepository.findOne(addressid);
		Route route = routeRepository.findOne(routeid);
		
		route.acceptDelivery(address);
		routeRepository.save(route);
	
		ModelAndView ret= new ModelAndView("route/onmap");
		ret.addObject("route", route);
		
		Address origin = route.getDeposit();
		Address destination = route.getCurrentAddress();

		ret.addObject("deposit", routeRepository.findOne(routeid).getDeposit());
		ret.addObject("alldeliveries", routeRepository.findOne(routeid).getAllDeliveries());
		ret.addObject("lastAddress", address);

		ret.addObject("deliveries", routeRepository.findOne(routeid).getOpenDeliveries());
		
		String[] addressesfinal = createAddressStringArray(routeid, true, false);
		ret.addObject("addresses", addressesfinal);
		
		ret.addObject("duration", origin.getDistanceStr(destination));
		
		return ret;
       
    }
	
	@PostMapping(value="/rejectDelivery/{addressid}")
    public ModelAndView rejectDelivery(@RequestParam long routeid, @PathVariable Long addressid) {
    
		Address address = addressRepository.findOne(addressid);
		Route route = routeRepository.findOne(routeid);
		
		route.rejectDelivery(address);
		routeRepository.save(route);
		
		
		ModelAndView ret= new ModelAndView("route/onmap");
		ret.addObject("route", route);
		
		Address origin = route.getDeposit();
		Address destination = route.getCurrentAddress();
		
		ret.addObject("deposit", routeRepository.findOne(routeid).getDeposit());
		ret.addObject("alldeliveries", routeRepository.findOne(routeid).getAllDeliveries());
		ret.addObject("lastAddress", address);

		ret.addObject("deliveries", routeRepository.findOne(routeid).getOpenDeliveries());
		
		String[] addressesfinal = createAddressStringArray(routeid, true, false);
		ret.addObject("addresses", addressesfinal);
		
		ret.addObject("duration", origin.getDistanceStr(destination));
		
		return ret;
       
    }
	
	@RequestMapping(path="/completeRoute/{routeid}")
	public String completeRouteOnMap(Model model, @PathVariable Long routeid) {
		
		Route route = routeRepository.findOne(routeid);
		model.addAttribute("route", route);

		// Origin Address of Route
		model.addAttribute("deposit", routeRepository.findOne(routeid).getDeposit());
		
		String[] addressesfinal = createAddressStringArray(routeid, false, false);
		model.addAttribute("addresses", addressesfinal);
		
		return "route/completeRoute";
	}
	
	/**
	 * starts the route, and if there is a problem, it sends it to an error page
	 * @param model
	 * @param routeid
	 * @return
	 */
	@PostMapping(value="/startRoute/{routeid}")
    public String startRoute(Model model, @PathVariable Long routeid){
    
		Route route = routeRepository.findOne(routeid);
		
		try {
			route.startRoute();
		}
		catch(Exception e)
		{
			model.addAttribute("message", e.getMessage());
			model.addAttribute("route", route);
			
			// Origin Address of Route
			model.addAttribute("deposit", routeRepository.findOne(routeid).getDeposit());
			
			String[] addressesfinal = createAddressStringArray(routeid, false, false);
			model.addAttribute("addresses", addressesfinal);
			
			return "route/completeRoute";
		}
		routeRepository.save(route);
	
		model.addAttribute("route", route);
		
		Address origin = route.getDeposit();
		Address destination = route.getCurrentAddress();

		// Origin Address of Route
		model.addAttribute("deposit", routeRepository.findOne(routeid).getDeposit());
		
		// All the deliveries (open or not)
		model.addAttribute("alldeliveries", routeRepository.findOne(routeid).getAllDeliveries());
		// When the route gets started, the last address is the deposit address of the route
		model.addAttribute("lastAddress", routeRepository.findOne(routeid).getDeposit());
		model.addAttribute("deliveries", routeRepository.findOne(routeid).getOpenDeliveries());
		
		String[] addressesfinal = createAddressStringArray(routeid, true, false);
		model.addAttribute("addresses", addressesfinal);
		
		model.addAttribute("duration", origin.getDistanceStr(destination));
		
		return "route/onmap";
       
    }


	/**
	 * creates an Array with addresses stored as Strings
	 * @param routeid
	 * @return
	 */
	private String[] createAddressStringArray(Long routeid, boolean includedeposit, boolean onlyOpen) {
		Set<Address> addresses = new HashSet<Address>();
		
		addresses =  routeRepository.findOne(routeid).getAllAddresses(includedeposit, onlyOpen);

		
		String[] addressesfinal = new String[addresses.size()];
		
		int i = 0;
		for(Address ad: addresses)
		{
			addressesfinal[i] =  ad.toString();
			i++;
		}
		return addressesfinal;
	}
}
