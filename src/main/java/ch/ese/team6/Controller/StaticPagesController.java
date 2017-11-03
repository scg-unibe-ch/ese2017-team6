package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.ese.team6.Model.Item;
import ch.ese.team6.Model.Truck;
import ch.ese.team6.Repository.ItemRepository;
import ch.ese.team6.Repository.TruckRepository;
import ch.ese.team6.Service.SampleDataService;

@Controller
public class StaticPagesController {
	@Autowired
	private TruckRepository truckRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private SampleDataService sampleData;
	
	@RequestMapping(path ="/")
	public String showHome() {
		return "staticpage/home";
	}
	
	@RequestMapping(path ="/sampleData")
	public String generateTestData(@RequestParam String admin) {
		sampleData.loadData();
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
