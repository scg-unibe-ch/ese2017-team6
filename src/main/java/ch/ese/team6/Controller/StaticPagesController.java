package ch.ese.team6.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.ese.team6.Model.Item;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Model.User;
import ch.ese.team6.Repository.ItemRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Service.UserService;

@Controller
public class StaticPagesController {
	@Autowired
	private UserService userService;
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private ItemRepository itemRepository;
	
	   
	@RequestMapping(path ="/")
	public String showHome() {
		return "staticpage/index";
	}
	
	@RequestMapping(path ="/admin")
	public String showAdmin() {
		return "staticpage/admin";
	}
	
	@RequestMapping(path ="/sampleData")
	public String generateTestData(@RequestParam String admin) {
		//if(admin== "generate") {}
		User user = new User();
		user.setUsername("ivanm");
		user.setFirstname("Ivan");
		user.setSurname("Mann");
		user.setEmail("mann@example.com");
		user.setPassword("password");
		user.setPasswordConfirm("password");
		userService.save(user);	
		
		Truck truck = new Truck();
		truck.setTruckname("VW 1");
		truck.setMaxCargoSpace(1);
		truck.setMaxLoadCapacity(1);
		truck.setVehicleCondition(0);
		truckRepository.save(truck);
		
		Item item = new Item();
		item.setName("Schraubenzieher");
		itemRepository.save(item);
		return "redirect:route/addtest";
	}
	

}
