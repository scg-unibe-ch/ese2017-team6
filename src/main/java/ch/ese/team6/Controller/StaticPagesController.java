package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
		return "staticpage/home";
	}
	
	@RequestMapping(path ="/sampleData")
	public String generateTestData(@RequestParam String admin) {
		String userCsv = "ivanm,Ivan,Mann,mann@example.com,0798199110,password, password;username,firstname,surname,email,password, password;";
		String[] users = userCsv.split(";");
		for(int i = 0; i < users.length; i++) {
			String[] userdata = users[i].split(",");
			User user = new User();
			user.setUsername(userdata[0]);
			user.setFirstname(userdata[1]);
			user.setSurname(userdata[2]);
			user.setEmail(userdata[3]);
			user.setPhone(userdata[4]);
			user.setPassword(userdata[5]);
			user.setPasswordConfirm(userdata[6]);
			userService.save(user);	
		}
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
