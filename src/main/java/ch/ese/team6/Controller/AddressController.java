package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.InvalidAddressException;
import ch.ese.team6.Model.Address;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Service.AddressService;

@Controller
@RequestMapping("/address")
public class AddressController {
		
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired  
	private AddressService addressService;
	
	
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
	public String addNewAddress (Model model, @ModelAttribute Address addressValue, BindingResult bindingResult) {
		if(!addressValue.isOK()) {
			return "address/error";
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("address", addressValue);
			return "address/createForm";}
		
		try {
			
			// Store the Address, method will throw exceptions if it's doesn't added properly
			//because it is not connected to the other addresses in this case the createDomicilAddress Window shows up
			addressService.save(addressValue);
		} catch (BadSizeException | InvalidAddressException e) {
			model.addAttribute("address", addressValue);
			//e.printStackTrace();
			return "address/createDomicilAddress";
		}
		model.addAttribute("address", addressRepository.findAll());
		return "address/addressIndex";
	}
	
	@PostMapping(path="/createDomicilAddress")
	public String addNewDomicilAddress (Model model, @ModelAttribute Address addressValue, BindingResult bindingResult) {
		if(!addressValue.isOK()) {
			return "address/error";
		}
		addressRepository.save(addressValue);

		model.addAttribute("address", addressRepository.findAll());
		return "address/addressIndex";
	}
	
	
	
	@GetMapping(path = "/{Id}")
	public String showAddress(Model address,@PathVariable Long Id) {
		address.addAttribute("address", addressRepository.findOne(Id));
		return "address/profile"; 
	}
	
	@GetMapping(path = "/{addressId}/edit")
	public String editAddress(Model address, @PathVariable long addressId) {
		address.addAttribute("address", addressRepository.findOne(addressId));
		address.addAttribute("statusArray", DataStatus.values());
		return "address/editForm";
	}
	
	@PostMapping(path = "/{addressId}/edit")
	public String editAddress(Model model, @ModelAttribute Address addressvalue, @PathVariable long addressId) {
		Address address = addressRepository.findOne(addressId);
		address.setStatus(addressvalue.getStatus());
		addressRepository.save(address);
		model.addAttribute("address", address);
		return ("address/profile");
	}	
	
	
}
