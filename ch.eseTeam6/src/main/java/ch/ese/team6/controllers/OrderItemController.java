package ch.ese.team6.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.models.orderitems.OrderItemRepository;
import ch.ese.team6.models.orderitems.OrderItems;

@Controller
@RequestMapping("/orderitem")
public class OrderItemController {
	
	@Autowired
	private OrderItemRepository orderitemRepository;
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		// add an Attribute called 'item', and fill it with an empty Item-Object
		  model.addAttribute("orderitems", new OrderItems());
	        return "orderitem/create";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewItem(@ModelAttribute OrderItems itemvalue) {
		
		OrderItems orderitem = new OrderItems();
		orderitem = itemvalue;
		orderitemRepository.save(orderitem);
		return new ModelAndView("/orderitem/show", "orderitem", orderitem);
	}
	
	@RequestMapping(path="/")
	public String showAllUsers(Model model) {
		model.addAttribute("orderitems", orderitemRepository.findAll());
		return "orderitem/index";
	}

}
