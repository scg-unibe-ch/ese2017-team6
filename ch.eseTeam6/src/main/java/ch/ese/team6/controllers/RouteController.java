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
	public String createForm(Model model, @RequestParam Date routeDate) {
		model.addAttribute("route", new RouteHelper(routeDate));
	        return "route/createForm";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewRoute (@ModelAttribute Routes routevalue) {
		Routes route = new Routes();
		route=routevalue;
		routeRepository.save(route);
		return new ModelAndView("/route/profile", "route", route);
	}
	
	@GetMapping(path="/addtest")
	public String addSampleRoutes() {
		for (long i= 1;i<4; i++) {
			Routes route = new Routes();
			route.setTruck(truckRepository.findOne(i));
			route.setDriver(userRepository.findOne(i));
			route.setDeliveryId(i); 
			route.setRouteDate(Calendar.getInstance());
			routeRepository.save(route);
		}
		return "/hello";
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
