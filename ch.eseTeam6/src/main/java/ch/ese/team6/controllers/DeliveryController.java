package ch.ese.team6.controllers;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.models.Deliveries.Delivery;
import ch.ese.team6.models.Deliveries.DeliveryRepository;
import ch.ese.team6.models.customers.CustomerRepository;
import ch.ese.team6.models.items.ItemRepository;
import ch.ese.team6.models.orderitems.OrderItemRepository;
import ch.ese.team6.models.orderitems.OrderItems;
import ch.ese.team6.models.orders.OrderRepository;
import ch.ese.team6.models.orders.Orders;
import ch.ese.team6.models.trucks.Trucks;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {
	
	@Autowired
	private DeliveryRepository deliveryRepository;
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
		model.addAttribute("deliveries", deliveryRepository.findAll());
		return "/delivery/deliveryMain";
	}
	
	
	@GetMapping(path="/{orderId}/createDelivery")
	public String addItems(Model model, @PathVariable Long orderId) {
		Delivery delivery = new Delivery();
		model.addAttribute("delivery", delivery);
		model.addAttribute("order", orderRepository.findOne(orderId));
		model.addAttribute("orderItems", orderRepository.findOne(orderId).getOrderItems());
	    return "delivery/"+orderId+"/createDelivery";
	}
	


	
	

}
