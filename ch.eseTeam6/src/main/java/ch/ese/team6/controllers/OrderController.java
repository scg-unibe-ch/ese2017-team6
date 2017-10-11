package ch.ese.team6.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.ese.team6.models.clients.Client;
import ch.ese.team6.models.orders.OrderRepository;
import ch.ese.team6.models.orders.Orders;
import ch.ese.team6.models.users.UserRepository;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;
	
    @GetMapping(path = "/add")
    public String orderForm(Model model) {
        model.addAttribute("order", new Orders());
        //add drop down menu with registered clients
        return "createOrder";
    }

    @PostMapping("/add")
    public String orderSubmit(@ModelAttribute Orders order) {
        return "orderResult";
    }
	
	@RequestMapping(path="/")
	public String showAllOrders(Model model) {
		model.addAttribute("orders", orderRepository.findAll());
		return "orderMain";
	}


}