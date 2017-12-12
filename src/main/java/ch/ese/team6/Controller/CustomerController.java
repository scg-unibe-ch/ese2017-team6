package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Model.Customer;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.CustomerRepository;
import ch.ese.team6.Service.AddressService;

@Controller
@RequestMapping("/customer")
public class CustomerController {
		
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressService addressService;
	
	
	
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("customer", new Customer());
		  model.addAttribute("allAddress",addressRepository.findAll());
		  model.addAttribute("allDAddress",addressService.findAllAddressesReachableByTruck());
	        return "customer/createForm";
	}

	
	@PostMapping(path="/add")
	public String addNewCustomer (@ModelAttribute Customer customervalue) {
		if(!customervalue.isOK()) {
    		return "customer/error";
    	}
    	
		customerRepository.save(customervalue);
		return "redirect:/customer/";
	}
	
	@RequestMapping(path="/")
	public String showAllCustomers(Model model) {
		model.addAttribute("customer", customerRepository.findAll());
		return "customer/customerIndex";
	}
	
	
	@GetMapping(path = "/{customerId}/edit")
	public String editCustomerForm(Model customer, @PathVariable long customerId) {
		customer.addAttribute("customer", customerRepository.findOne(customerId));
		customer.addAttribute("allAddress",addressRepository.findAll());
		customer.addAttribute("allDAddress",addressService.findAllAddressesReachableByTruck());
		customer.addAttribute("statusArray", DataStatus.values());
		return "customer/editForm";
	}
	
	
	@PostMapping(path = "/{Id}/edit")
	public ModelAndView editUser (@ModelAttribute Customer customervalue, @PathVariable long Id) {
		Customer cus = customerRepository.findOne(Id);
		try {
		cus.setName(customervalue.getName());
		cus.setAddress(customervalue.getAddress());
		cus.setDomicilAddress(customervalue.getDomicilAddress());
		cus.setStatus(customervalue.getStatus());
		if(!customervalue.isOK()) {
    		return new ModelAndView("customer/error");
    	}		
		customerRepository.save(cus);
		} catch(Exception e) {}
		return new ModelAndView("customer/profile", "customer", cus);
	}
	
	

	@GetMapping(path = "/{Id}")
	public String showUser(Model customer,@PathVariable Long Id) {
		customer.addAttribute("customer", customerRepository.findOne(Id));
		return "customer/profile"; 
	}
	
	
	@GetMapping(path ="/edit")
	public String editCustomerForm(Model customer) {
		
		return "customer/editForm";
	}
	
}
