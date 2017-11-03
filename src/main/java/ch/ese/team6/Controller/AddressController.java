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

import ch.ese.team6.Model.Address;
import ch.ese.team6.Repository.AddressRepository;

@Controller
@RequestMapping("/address")
public class AddressController {
		
	@Autowired
	private AddressRepository addressRepository;
	
	
	@RequestMapping(path="/")
	public String showAllAdress(Model model) {
		model.addAttribute("address", addressRepository.findAll());
		return "address/addressIndex";
	}
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("address", new Address());
	        return "address/createForm";
	}

	
	@PostMapping(path="/add")
	public String addNewAddress (@ModelAttribute Address addressValue) {
		if(!addressValue.isOK()) {
			return "address/error";
		}
		addressRepository.save(addressValue);
		return "address/addressIndex";
	}
	
	
	
}