package ch.ese.team6.controllers;

import java.io.IOException;
import java.util.ArrayList;
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
@RequestMapping("/order")
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
	
	@GetMapping(path = "/add")
	public String selectCustomer(Model model) {
		model.addAttribute("Order",new Orders());
		model.addAttribute("allCustomers", customerRepository.findAll());
		model.addAttribute("allItems", itemRepository.findAll());
		return "/orders/initializeOrder";
	}
	
	 @RequestMapping(path="/add", params={"addRow"})
	    public String addRow(final Orders order, final BindingResult bindingResult, Model model) {
		 	
	        order.getOrderItems().add(new OrderItems());
	        System.out.println("Hier");
	        return "/orders/initializeOrder";
	    }
	    
	    
	    @RequestMapping(path="/add", params={"removeRow"})
	    public String removeRow(final Orders order, final BindingResult bindingResult, final HttpServletRequest req) {
	        final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	        order.getOrderItems().remove(rowId.intValue());
	        return "/orders/initializeOrder";
	    }
	
	    @RequestMapping(path="/add", params={"save"})
	    public String saveSeedstarter(final Orders order, final BindingResult bindingResult, final ModelMap model) {
	        if (bindingResult.hasErrors()) {
	        	return "/orders/initializeOrder";
	        }
	        this.orderRepository.save(order);
	       
	        return "redirect:/orders/initializeOrder";
	    }
	    
	
//	
//	@PostMapping("/add")
//	public ModelAndView orderSubmit(@ModelAttribute Orders order, HttpServletResponse response) {
//		Orders newOrder = new Orders(order.getClientName(), order.getDeliveryAddress(), order.getDeliveryDate());
//		orderRepository.save(newOrder);
//		return new ModelAndView("/orders/profile", "order", newOrder);
//	}

	

	@GetMapping(path = "/{orderId}")
	public String showOrder(Model order, @PathVariable Long orderId) {
		order.addAttribute("order", orderRepository.findOne(orderId));
		return "orders/profile";
	}

	@GetMapping(path = "/{orderId}/edit")
	public String editOrder(Model order, @PathVariable long orderId) {
		order.addAttribute("order", orderRepository.findOne(orderId));
		return "orders/edit";
	}

	/*
	@PostMapping(path = "/{orderId}/edit")
	public ModelAndView editorder(@ModelAttribute Orders ordervalue, @PathVariable long orderId) {
		Orders order = orderRepository.findOne(orderId);
		order.setDeliveryAddress(ordervalue.getDeliveryAddress());
		orderRepository.save(order);
		return new ModelAndView("/orders/profile", "order", order);
	}*/

	@DeleteMapping(path = "/{orderId}/edit")
	public void deleteorder(@PathVariable long orderId, HttpServletResponse response) {
		orderRepository.delete(orderId);
		try {
			response.sendRedirect("/order/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}