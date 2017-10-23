package ch.ese.team6.controllers;

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
	
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		Delivery delivery = new Delivery();
		
		model.addAttribute("delivery", delivery);
		model.addAttribute("allOrders", orderRepository.findAll());
	    return "delivery/add";
	}
	
	
	
	@RequestMapping(path="/add", params={"select"})
    public String saveDelivery(final Delivery delivery, final BindingResult bindingResult, final ModelMap model) {
        this.deliveryRepository.save(delivery);
        
        return "redirect:/delivery/{deliveryId}/addOrderItems";
    }
	
	@GetMapping(path = "/{deliveryId}")
	public String showOrder(Model order, @PathVariable Long deliveryId) {
		order.addAttribute("delivery", deliveryRepository.findOne(deliveryId));
		return "delivery/profile";
	}
	
	
	@GetMapping(path="/{deliveryId}/addOrderItems")
	public String addItems(Model model, @PathVariable Long deliveryId) {
		
		model.addAttribute("delivery", deliveryRepository.findOne(deliveryId));
		model.addAttribute("allOrderItems", deliveryRepository.findOne(deliveryId).getOrder().getOrderItems());
	      return "delivery/{deliveryId}/addOrderItems";
	}
	
	@RequestMapping(path="/{deliveryId}/addOrderItems", params= {"save"})
	public String saveOrderItems(Model model, @PathVariable Long deliveryId) {
		
		model.addAttribute("delivery", deliveryRepository.findOne(deliveryId));
		model.addAttribute("allOrderItems", deliveryRepository.findOne(deliveryId).getOrder().getOrderItems());
	      return "delivery/{deliveryId}/addOrderItems";
	}
	

	
	

}
