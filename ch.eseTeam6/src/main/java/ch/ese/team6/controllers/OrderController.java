package ch.ese.team6.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ch.ese.team6.models.orders.OrderRepository;
import ch.ese.team6.models.orders.Orders;
import ch.ese.team6.models.users.UserRepository;

@Controller
@org.springframework.web.bind.annotation.RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;

    @GetMapping(path = "/order")
    public String orderForm(Model model) {
        model.addAttribute("order", new Orders());
        return "initializeOrder";
    }

    @PostMapping("/order")
    public String orderSubmit(@ModelAttribute Orders order) {
        return "orderResult";
    }

}