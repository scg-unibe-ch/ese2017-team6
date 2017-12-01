package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;
import ch.ese.team6.Service.SampleDataService;

@Controller
public class StaticPagesController {
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
	public String generateTestData(Model model, String error, String message) {
		try {
			sampleData.loadData();
		} catch (BadSizeException | DupplicateEntryException e) {
			model.addAttribute("error", "Data coudn't load!\n"
					+ "" + e.getMessage());
			return "staticpage/loaded";
		}
		model.addAttribute("message", "Loaded data!");
		return "staticpage/loaded";
	}
}
