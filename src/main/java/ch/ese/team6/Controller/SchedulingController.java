package ch.ese.team6.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Exception.InconsistentOrderStateException;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.RouteStatus;
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
	@Autowired
	private RouteRepository truckRepository;
	@Autowired
	private RouteRepository userRepository;
	@Autowired
	private RouteRepository addressRepository;
	
	
	@GetMapping(path="/scheduleOrders")
	public String scheduleEverything(Model model) {
		model.addAttribute("trucks", truckRepository.findAll());
		model.addAttribute("drivers", userRepository.findAll());
		model.addAttribute("addresses", addressRepository.findAll());
		model.addAttribute("orders", orderRepository.findAll());
		model.addAttribute("routes", routeRepository.findAll());
	    return "schedule/scheduleOrders";
	}
	
	@RequestMapping(value="/scheduleOrders", params={"orderid"})
    public ModelAndView addRow(final Route route, final BindingResult bindingResult, @RequestParam Order order) {
    	
		if(route.doesIDelivarableFit(order))
			{route.addDelivarable(order);
			routeRepository.save(route);}
    	
        ModelAndView ret = new ModelAndView("schedule/scheduleOrders");
        ret.addObject("trucks", truckRepository.findAll());
		ret.addObject("drivers", userRepository.findAll());
		ret.addObject("addresses", addressRepository.findAll());
		ret.addObject("orders", orderRepository.findAll());
		ret.addObject("routes", routeRepository.findAll());

		return ret;
       
    }
	
	@GetMapping(path="/")
	public String overview(Model model) {
		model.addAttribute("orders", orderRepository.findAll());
		model.addAttribute("routes", routeService.selectWithLeftCapacity());
		return "schedule/overview";
	}
	
	@GetMapping(path ="/{orderId}")
	public String addOrderToRoute (Model model, @PathVariable long orderId) {
		Order order = orderRepository.findOne(orderId);
		model.addAttribute("order", order);
		
		List<Route> allroutes = routeService.selectWithLeftCapacity(order);
		
		List<Route> routes = new ArrayList<Route>();
		
		for(Route r : allroutes)
		{
			if(r.getRouteStatus().equals(RouteStatus.OPEN))
				routes.add(r);
		}
		
		model.addAttribute("routes", routes);
		return "schedule/addOrder";
	}
	
	@PostMapping(path ="/{orderId}")
	public String createDelivery(Model model, @PathVariable long orderId, @RequestParam String routeId) {
		long routeIdl = Long.parseLong(routeId.replace("Add order to route ",""));
		
		Route route = routeRepository.findOne(routeIdl);
		Order order = orderRepository.findOne(orderId);
	
		if (!route.doesIDelivarableFit(order)) return"redirect:/schedule/"+orderId;
		
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
		return "redirect:/orders/"+order.getId();
	}
	
	@GetMapping(path="/addOrderstoRoute/{routeid}")
	public String addOrderstoRoute(Model model, @PathVariable long routeid)
	{
		
		List<Order> orders = new ArrayList<Order>();
		
		for(Order o : orderRepository.findAll())
		{
			if(routeRepository.findOne(routeid).doesIDelivarableFit(o) && o.isOpen())
				{orders.add(o);}
		}
		
		model.addAttribute("route", routeRepository.findOne(routeid));
		model.addAttribute("orders", orders);
		
		return "schedule/addOrderstoRoute";
	}
	
	@PostMapping(path="/addOrderstoRoute/{routeid}", params={"orderID"})
	public String addOrderstoRoute(Model model, @PathVariable long routeid, @RequestParam String orderID) throws InconsistentOrderStateException
	{
		long orderId = Long.parseLong(orderID.replace("add order number ",""));
		
		Route route = routeRepository.findOne(routeid);
		Order o = orderRepository.findOne(orderId);
		
		if(o.isOpen() && route.doesIDelivarableFit(o))
			{route.addDelivarable(o);
			o.schedule();
			orderRepository.save(o);
			routeRepository.save(route);}
		
		List<Order> orders = new ArrayList<Order>();
		
		for(Order order : orderRepository.findAll())
		{
			if(route.doesIDelivarableFit(order) && order.isOpen())
				{orders.add(order);}
		}
		
		model.addAttribute("route", route);
		model.addAttribute("orders", orders);
		
		return "schedule/addOrderstoRoute";
	}
	
	
}