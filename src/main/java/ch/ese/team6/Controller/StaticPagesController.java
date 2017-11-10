package ch.ese.team6.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	private boolean testing = true;
	   
	@RequestMapping(path ="/")
	public String showHome() {
		if(testing) return "redirect:/sampleData";
		return "staticpage/index";
	}
	
	@RequestMapping(path ="/admin")
	public String showAdmin() {
		return "staticpage/admin";
	}
	
	@RequestMapping(path ="/sampleData")
	public String generateTestData() {
		sampleData.loadData();
		return "redirect:/";
	}
	
	

}
