package ch.ese.team6.controllers;

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

import ch.ese.team6.models.customer.Customer;
import ch.ese.team6.models.customer.CustomerRepository;


@Controller
@RequestMapping("/customer")
public class CustomerController {
		
	@Autowired
	private CustomerRepository customerRepository;
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		  model.addAttribute("customer", new Customer());
	        return "customer/createForm";
	}

	
	@PostMapping(path="/add")
	public String addNewCustomer (@ModelAttribute Customer customervalue) {
		customerRepository.save(customervalue);
		return "customer/customerIndex";
	}
	
	@RequestMapping(path="/")
	public String showAllCustomers(Model model) {
		model.addAttribute("customer", customerRepository.findAll());
		return "customer/customerIndex";
	}
	
	@GetMapping(path = "/{customerId}/edit")
	public String editCustomerForm(Model customer, @PathVariable long customerId) {
		customer.addAttribute("customer", customerRepository.findOne(customerId));
		return "customer/editForm";
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
	
	@GetMapping(path ="/edit")
	public String editCustomerForm(Model customer) {
		
		return "customer/editForm";
	}
	
}
