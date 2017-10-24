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

import ch.ese.team6.Model.Route;
import ch.ese.team6.Repository.RouteRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Repository.UserRepository;

@Controller
@RequestMapping("/route")
public class RouteController {
		
	@Autowired
	private RouteRepository routeRepository;
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private UserRepository userRepository;
	
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
		return "/";
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
	
}
