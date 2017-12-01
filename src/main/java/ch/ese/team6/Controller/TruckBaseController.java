package ch.ese.team6.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Service.TruckService;


@Controller
@RequestMapping("/truck")
public class TruckBaseController {
		
	@Autowired	private TruckRepository truckRepository;
	@Autowired	private TruckService truckService;
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("truck", new Truck());
		  model.addAttribute("statusArray", DataStatus.values());
	        return "truck/create";
	}

	
	@PostMapping(path="/add")
	public String addNewTruck (Model model, @ModelAttribute("truck")@Valid Truck truck, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("truck", truck);
			model.addAttribute("statusArray", DataStatus.values());
			return "truck/create";
		}
		try {
			truckService.save(truck);
		} catch (DupplicateEntryException | BadSizeException e) {
			model.addAttribute("message", e.getMessage());
			model.addAttribute("truck", truck);
			return "truck/create";
		}
		model.addAttribute("truck",truck);
		return "truck/profile";
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
		truck.addAttribute("statusArray", DataStatus.values());
		return "truck/edit";
	}
	
	@PostMapping(path = "/{truckId}/edit")
	public String editTruck (Model model, @ModelAttribute Truck truckvalue, @PathVariable long truckId) {
		Truck truck = truckRepository.findOne(truckId);
		try {
			truck.setTruckname(truckvalue.getTruckname());
			truck.setStatus(truckvalue.getStatus());
		} catch (BadSizeException e) {
			// TODO Auto-generated catch block
			return "staticpage/loaded";
		}
		
		truckRepository.save(truck);
		model.addAttribute("truck", truck);
		return ("truck/profile");
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
