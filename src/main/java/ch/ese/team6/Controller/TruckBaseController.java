package ch.ese.team6.Controller;

import java.io.IOException;

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
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Model.Truck;
import ch.ese.team6.Repository.TruckRepository;


@Controller
@RequestMapping("/truck")
public class TruckBaseController {
		
	@Autowired
	private TruckRepository truckRepository;
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("truck", new Truck());
	        return "truck/create";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewTruck (@ModelAttribute Truck truckvalue) {
		Truck truck = new Truck();
		truck=truckvalue;
		truckRepository.save(truck);
		return new ModelAndView("/truck/profile", "truck", truck);
	}
	
	@RequestMapping(path="/")
	public String showAllTruck(Model model) {
		model.addAttribute("trucks", truckRepository.findAll());
		return "truck/index";
	}
	
	
	@GetMapping(path = "/{truckId}")
	public String showTruck(Model truck,@PathVariable Long truckId) {
		truck.addAttribute("truck", truckRepository.findOne(truckId));
		return "truck/profile"; 
	}
	
	@GetMapping(path = "/{truckId}/edit")
	public String editTruck(Model truck, @PathVariable long truckId) {
		truck.addAttribute("truck", truckRepository.findOne(truckId));
		return "truck/edit";
	}
	
	@PostMapping(path = "/{truckId}/edit")
	public ModelAndView editTruck (@ModelAttribute Truck truckvalue, @PathVariable long truckId) {
		Truck truck = truckRepository.findOne(truckId);
		truck.setTruckname(truckvalue.getTruckname());truck.setVehicleCondition(truckvalue.getVehicleCondition());
		truckRepository.save(truck);
		return new ModelAndView("/truck/profile", "truck", truck);
	}
	
	@DeleteMapping(path = "/{truckId}/edit")
	public void deleteTruck(@PathVariable long truckId, HttpServletResponse response) {
		truckRepository.delete(truckId);
		try {
			response.sendRedirect("/truck/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
