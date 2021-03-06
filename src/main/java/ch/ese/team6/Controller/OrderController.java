package ch.ese.team6.Controller;

import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Model.Order;
import ch.ese.team6.Model.OrderItem;
import ch.ese.team6.Repository.CustomerRepository;
import ch.ese.team6.Repository.ItemRepository;
import ch.ese.team6.Repository.OrderRepository;


@Controller
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ItemRepository itemRepository;

	
	@RequestMapping(path = "/")
	public String showAllOrders(Model model) {
		model.addAttribute("orders", orderRepository.findAll());
		return "orders/orderMain";
	}
	
	
	
	@GetMapping(path = "/add")
	public String newOrder(Model model) {
		Order o = new Order();
		o.setDeliveryDate(Calendar.getInstance().getTime());
		model.addAttribute("order",o);
		
		model.addAttribute("allCustomers", customerRepository.findAll());
		model.addAttribute("allItems", itemRepository.findAll());
		return "orders/add";
	}
	
    @RequestMapping(value="/add", params={"addRow"})
    public ModelAndView addRow(final Order order, final BindingResult bindingResult) {
    	
    	order.getOrderItems().add(new OrderItem(order));
    	
        ModelAndView ret = new ModelAndView("orders/add");
		ret.addObject("order",order);
    	ret.addObject("allCustomers", customerRepository.findAll());
		ret.addObject("allItems", itemRepository.findAll());

		return ret;
       
    }
	
	    @RequestMapping(path="/add", params={"removeRow"})
	    public ModelAndView removeRow(final Order order, final BindingResult bindingResult, final HttpServletRequest req) {
	    	
	    	final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	        order.getOrderItems().remove(rowId.intValue());
	   
	       
	        ModelAndView ret = new ModelAndView("orders/add");
			ret .addObject("order",order);
	    	ret.addObject("allCustomers", customerRepository.findAll());
			ret.addObject("allItems", itemRepository.findAll());

			return ret;
	       
	    }
	
	    @RequestMapping(path="/add", params={"save"})
	    public String saveOrder(final Order order, final BindingResult bindingResult, final ModelMap model) {
	       
	    	if(!order.isOK()) {
	    		return "orders/error";
	    	}
	    	
	    	this.orderRepository.save(order);

	    	return "redirect:/orders/"+order.getId();
	    }
	    

	@GetMapping(path = "/{orderId}")
	public String showOrder(Model order, @PathVariable Long orderId) {
		order.addAttribute("order", orderRepository.findOne(orderId));
		return "orders/profile";
	}

}