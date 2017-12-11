package ch.ese.team6.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Exception.InconsistentOrderStateException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.AddressDistanceManager;
import ch.ese.team6.Model.IDelivarable;
import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderStatus;
import ch.ese.team6.Model.Route;
import ch.ese.team6.Model.RouteCollection;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.OrderRepository;
import ch.ese.team6.Repository.RoleRepository;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;
import ch.ese.team6.Service.CalendarService;
import ch.ese.team6.Service.OrderService;
import ch.ese.team6.Service.OurCompany;
import ch.ese.team6.Service.RouteGenerationProblem;
import ch.ese.team6.Service.RouteGenerator;
import ch.ese.team6.Service.TruckService;
import ch.ese.team6.Service.UserService;

@Controller
@RequestMapping("/automaticroutegeneration")
public class AutomaticRouteController {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private TruckService truckService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AddressRepository addressRepository;
	
	@GetMapping(path = "/selectdate")
	public String selectDate(Model model) {

		// This method lets the customer select a date where he can start an automatic
		// route generation
		// We will propose him only dates where there are order ready to shedule.
		List<Date> dates = orderService.findAllDatesWithOpenOrders();
		// Dates are all the dates where they are routes

		RouteGenerationProblem routeProblem = new RouteGenerationProblem();

		/**
		 * This already proposes the first date 
		 */
		if (!dates.isEmpty()) {
			routeProblem.setDeliveryDate(dates.get(0));
			model.addAttribute("routeProblem", routeProblem);
		}
		
		List<String> formattedDates = new ArrayList<String>(dates.size());
		for(Date d: dates) {
			formattedDates.add(CalendarService.format(d));
		}
		model.addAttribute("dates", formattedDates);


		return "automaticroutegeneration/selectdate";
	}

	@RequestMapping(value = "/selectdate", params = { "submitDate" })
	public ModelAndView submitDate(final RouteGenerationProblem routeProblem, final BindingResult bindingResult) {

		Date deliveryDate = routeProblem.getDeliveryDate();
		ArrayList<IDelivarable> openOrders = new ArrayList<IDelivarable>(orderService.findOpenOrders(deliveryDate));
		if(openOrders.isEmpty()) {
			return new ModelAndView("automaticroutegeneration/selectdate");

		}
		
		
		List<Truck> trucks = truckService.findFreeTrucks(deliveryDate);
		List<User> drivers = userService.findFreeDrivers(deliveryDate);

		// routeProblem.setAddressManager(new AddressDistanceDummyManager());
		routeProblem.setOrders(openOrders);
		routeProblem.setTrucks(trucks);
		routeProblem.setDrivers(drivers);
		routeProblem.setAddressManager(new AddressDistanceManager());
		routeProblem.setDepositAddress(addressRepository.findOne(OurCompany.depositId));

		RouteGenerator rg = new RouteGenerator();
		rg.initialize(routeProblem);
		RouteCollection optimalRoutes = rg.getRoutes();// Will be null if no optimal solution

		ModelAndView ret = null;
		if (optimalRoutes == null) {
			// optimalRoutes = null means the Algorithm was not able to solve the problem

			ret = new ModelAndView("automaticroutegeneration/nosolution");
			ret.addObject("routeProblem", routeProblem);
		} else {
			// The solver has found a solution, we save the solution to the database
			ret = new ModelAndView("automaticroutegeneration/solution");

			optimalRoutes.removeEmptyRoutes();

			for (Route route : optimalRoutes.getRoutes()) {

				routeRepository.save(route);

			}
			
			ret.addObject("optimalRoutes", optimalRoutes);

			for (IDelivarable o : openOrders) {
				Order or = (Order) o;

				Order orts = orderRepository.findOne(or.getId());
				try {
					orts.schedule();
				} catch (InconsistentOrderStateException e) {
					e.printStackTrace();
				}
				orderRepository.save(or);
			}

		}

		return ret;

	}

	



}