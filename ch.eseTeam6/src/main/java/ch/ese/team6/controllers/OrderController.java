package ch.ese.team6.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.models.customers.Customer;
import ch.ese.team6.models.customers.CustomerRepository;
import ch.ese.team6.models.items.ItemRepository;

import ch.ese.team6.models.orderitems.OrderItemRepository;
import ch.ese.team6.models.orderitems.OrderItems;
import ch.ese.team6.models.orders.OrderHelper;
import ch.ese.team6.models.orders.OrderRepository;
import ch.ese.team6.models.orders.Orders;



@Controller
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;

	
	@RequestMapping(path = "/")
	public String showAllOrders(Model model) {
		model.addAttribute("orders", orderRepository.findAll());
		return "/orders/orderMain";
	}
	
	/*
	 * This function creates testdata
	 */
	@GetMapping(path="/addtest")
	public String addSampleOrders() {
		
		for (int i= 0;i<10; i++) {
			
			int n_cus = customerRepository.findAll().size();
			Customer cus = customerRepository.findAll().get((int) (Math.random()*n_cus));
			Date deliveryDate = Calendar.getInstance().getTime();
			
			Orders order = new Orders();
			order.setCustomer(cus);
			order.setDeliveryDate(deliveryDate);
			
			for(int j=0;j<(int)(Math.random()*10+1);j++){
				OrderItems oi = new OrderItems(order);
				order.getOrderItems().add(oi);
				oi.setAmount((int)(Math.random()*10));
				int n_item = itemRepository.findAll().size();
				oi.setItem(itemRepository.findAll().get((int) (Math.random()*n_item)));
				
				orderItemRepository.save(oi);
		
			}
			
			
			orderRepository.save(order);
		}
		return "redirect:/hello";
	}
	
	
	@GetMapping(path = "/add")
	public String newOrder(Model model) {
		Orders o = new Orders();
		o.getOrderItems().add(new OrderItems(o));
		o.setDeliveryDate(Calendar.getInstance().getTime());
		model.addAttribute("order",o);
		
		model.addAttribute("allCustomers", customerRepository.findAll());
		model.addAttribute("allItems", itemRepository.findAll());
		return "/orders/add";
	}
	

	
    @RequestMapping(value="/add", params={"addRow"})
    public ModelAndView addRow(final Orders order, final BindingResult bindingResult) {
    	
    	order.getOrderItems().add(new OrderItems(order));
    	
        ModelAndView ret = new ModelAndView("/orders/add");
		ret .addObject("order",order);
    	ret.addObject("allCustomers", customerRepository.findAll());
		ret.addObject("allItems", itemRepository.findAll());

		
		
		return ret;
       
    }
	
   
	    
	    @RequestMapping(path="/add", params={"removeRow"})
	    public ModelAndView removeRow(final Orders order, final BindingResult bindingResult, final HttpServletRequest req) {
	    	
	    	final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	        order.getOrderItems().remove(rowId.intValue());
	   
	       
	        ModelAndView ret = new ModelAndView("/orders/add");
			ret .addObject("order",order);
	    	ret.addObject("allCustomers", customerRepository.findAll());
			ret.addObject("allItems", itemRepository.findAll());

			
			
			return ret;
	       
	    }
	
	    @RequestMapping(path="/add", params={"save"})
	    public String saveOrder(final Orders order, final BindingResult bindingResult, final ModelMap model) {
	        this.orderRepository.save(order);

	       
	        return "redirect:/orders/";
	    }
	    
	


	@GetMapping(path = "/{orderId}")
	public String showOrder(Model order, @PathVariable Long orderId) {
		order.addAttribute("order", orderRepository.findOne(orderId));
		return "orders/profile";
	}

	



}