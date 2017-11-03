package ch.ese.team6.Controller;

import java.io.IOException;
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
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Model.Customer;
import ch.ese.team6.Repository.AddressRepository;
import ch.ese.team6.Repository.CustomerRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {
		
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	/*
	 * This function creates testdata
	 */
	@GetMapping(path="/addtest")
	public String addSampleCustomers() {
		String[] names = {"sbb","swisscom","ibm","postfinance","google","brumBrumm"};
		for (int i= 0;i<names.length; i++) {
			Customer cus = new Customer();
			cus.setName(names[i]);
			int n = addressRepository.findAll().size();
			cus.setAddress(addressRepository.findAll().get(i%n));
			
			customerRepository.save(cus);
		}
		return "redirect:/orders/addtest";
	}
	
	
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("customer", new Customer());
		  model.addAttribute("allAddress",addressRepository.findAll());
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
		return "customer/editForm";
	}
	
	
	@PostMapping(path = "/{Id}/edit")
	public ModelAndView editUser (@ModelAttribute Customer customervalue, @PathVariable long Id) {
		Customer cus = customerRepository.findOne(Id);
		cus.setName(customervalue.getName());cus.setAddress(customervalue.getAddress());
		if(!customervalue.isOK()) {
    		return new ModelAndView("customer/error");
    	}
		
		customerRepository.save(cus);
		return new ModelAndView("/customer/profile", "customer", cus);
	}
	
	
	@DeleteMapping(path = "/{Id}/edit")
	public void deleteCustomer(@PathVariable long Id, HttpServletResponse response) {
		customerRepository.delete(Id);
		try {
			response.sendRedirect("/customer/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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