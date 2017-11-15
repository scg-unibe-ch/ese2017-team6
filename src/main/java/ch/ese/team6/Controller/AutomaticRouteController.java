package ch.ese.team6.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.boot.model.source.spi.Orderable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.AddressDistanceDummyManager;
import ch.ese.team6.Model.AddressDistanceManager;
import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Model.OrderStatus;
import ch.ese.team6.Model.Role;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.RouteCollection;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.CustomerRepository;
import ch.ese.team6.Repository.ItemRepository;
import ch.ese.team6.Repository.OrderItemRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Service.IAutomaticRouteGenerator;
import ch.ese.team6.Service.RouteGenerationProblem;
import ch.ese.team6.Service.RouteGenerator;
import ch.ese.team6.Service.TruckService;
import ch.ese.team6.Service.TruckServiceImpl;

@Controller
@RequestMapping("/automaticroutegeneration")
public class AutomaticRouteController {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
    private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRespository;
	@Autowired
	private RouteRepository routeRepository;
	
	@GetMapping(path = "/selectdate")
	public String selectDate(Model model) {

		// This method lets the customer select a date where he can start an automatic route generation
		// We will propose him only dates where there are order ready to shedule.
		List<Date> dates = new ArrayList<Date>();

		List<Order> orders = orderRepository.findAll();
		for (Order o : orders) {
			Date delDate = o.getDeliveryDate();
			if (o.getStatus().equals(OrderStatus.OPEN)&&!dates.contains(delDate)) {
				dates.add(delDate);
			}
		}
		
		// Dates are all the dates where they are routes
		model.addAttribute("dates", dates);
		
		model.addAttribute("allAddress",addressRepository.findAll());
		
		RouteGenerationProblem routeProblem = new RouteGenerationProblem();
		
		if (!dates.isEmpty()) {
			routeProblem.setDeliveryDate(dates.get(0));
			model.addAttribute("routeProblem", routeProblem);
		}
		
		
		return "automaticroutegeneration/selectdate";
	}

	@RequestMapping(value="/selectdate", params={"submitDate"})
    public ModelAndView submitDate(final RouteGenerationProblem routeProblem, final BindingResult bindingResult) {
    	
		
    	Date deliveryDate = routeProblem.getDeliveryDate();

    	//TODO: Does not work: find available trucks
        //	SimpleDateFormat df = new SimpleDateFormat("yyyy-M-dd");
    	//TruckService ts = new TruckServiceImpl();
    	//ArrayList<Truck> trucks = (ArrayList<Truck>) ts.findFreeTrucks(df.format(deliveryDate));
    	ArrayList<Truck> trucks = (ArrayList<Truck>) truckRepository.findAll();
    	//TODO: Does not work: find available drivers

    	
    	ArrayList<User> drivers = (ArrayList<User>) userRepository.findByRoles(roleRespository.findByName("DRIVER"));
    	
    	
    	ArrayList<IDelivarable> openOrders = new ArrayList<IDelivarable>(findOpenOrders(deliveryDate));
    	
    	
    	//routeProblem.setAddressManager(new AddressDistanceDummyManager());
		routeProblem.setOrders(openOrders);
		routeProblem.setTrucks(trucks);
		routeProblem.setDrivers(drivers);
        routeProblem.setAddressManager(new AddressDistanceDummyManager());
        
        RouteGenerator rg = new RouteGenerator();
		rg.initialize(routeProblem);
		RouteCollection optimalRoutes = rg.getRoutes();//Will be null if no optimal solution
		
		
		ModelAndView ret = null;
		if(optimalRoutes==null) {
			//optimalRoutes = null means the Algorithm was not able to solve the problem

			ret = new ModelAndView("automaticroutegeneration/optproblem");
			ret.addObject("routeProblem",routeProblem);
		}else {
			ret = new ModelAndView("automaticroutegeneration/solution");	

			
			optimalRoutes.removeEmptyRoutes();
			
			for(Route route:optimalRoutes.getRoutes()) {
	
					routeRepository.save(route);

			}
			ret.addObject("optimalRoutes",optimalRoutes);
			
			for(IDelivarable o: openOrders) {
				Order or = (Order)o;
				or.scheduleOrder();
				
				Order orts = orderRepository.findOne(or.getId());
				orts.scheduleOrder();
				orderRepository.save(or);
			}
			
			
			}
		
		
		return ret;
		
		
 
	}
	
	
	
	
	
	public List<Order> findOpenOrders(Date date){

		List<Order> orders = orderRepository.findAll();
		
		for(int i = orders.size()-1;i>=0;i--) {
			Order o = (Order) orders.get(i);
			if(!o.getStatus().equals(OrderStatus.OPEN)||date.before(o.getDeliveryDate()) || date.after(o.getDeliveryDate())) {
				orders.remove(o);
			}
			}
		return orders;
	}
	

}