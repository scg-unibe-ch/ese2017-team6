package ch.ese.team6.controllers;

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

import ch.ese.team6.models.routes.RouteHelper;
import ch.ese.team6.models.routes.RouteRepository;
import ch.ese.team6.models.routes.RouteSimple;
import ch.ese.team6.models.routes.Routes;
import ch.ese.team6.models.trucks.TruckRepository;
import ch.ese.team6.models.users.UserRepository;

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
		model.addAttribute("routeTemplate", new RouteSimple(date));
		model.addAttribute("routeDate", date);
		model.addAttribute("trucks", truckRepository.findAll());
		model.addAttribute("drivers", userRepository.findByUserrole(1));
	        return "route/createForm";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewRoute (@RequestParam Date routeDate, 
			@RequestParam int driverId, @RequestParam int truckId) {
		Routes newRoute = new Routes(Calendar.getInstance());
		newRoute.setRouteDate(routeDate);
		newRoute.setDeliveryId(10);
		newRoute.setDriver(userRepository.findOne((long) driverId));
		newRoute.setTruck(truckRepository.findOne((long) truckId));
		routeRepository.save(newRoute);
		return new ModelAndView("/route/profile", "route", newRoute);
	}
	/*
	 * This function creates testdata
	 */
	@GetMapping(path="/addtest")
	public String addSampleRoutes() {
		for (long i= 1;i<4; i++) {
			Routes route = new Routes(Calendar.getInstance());
			route.setTruck(truckRepository.findOne(i));
			route.setDriver(userRepository.findOne(i+6));
			route.setDeliveryId(i); 
			routeRepository.save(route);
		}
		return "/hello";
	}
	
	@GetMapping(path="/test")
	public String testListFuncions(Model model) {
		long number = 7;
		model.addAttribute(routeRepository.findByDriver(userRepository.findOne(number)));
		return "route/test";
	}
	
	@RequestMapping(path="/")
	public String showAllRoutes(Model model) {
		model.addAttribute("routes", routeRepository.findAll());
		return "route/routeIndex";
	}
	
	
	@GetMapping(path = "/{routeId}")
	public String showRoute(Model route,@PathVariable Long routeId) {
		route.addAttribute("route", routeRepository.findOne(routeId));
		return "route/profile"; 
	}
	
	@GetMapping(path = "/{routeId}/edit")
	public String editRoute(Model route, @PathVariable long routeId) {
		route.addAttribute("route", routeRepository.findOne(routeId));
		return "route/editForm";
	}
	
	@PostMapping(path = "/{routeId}/edit")
	public ModelAndView editRoute (@ModelAttribute Routes routevalue, @PathVariable long routeId) {
		Routes route = routeRepository.findOne(routeId);
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
