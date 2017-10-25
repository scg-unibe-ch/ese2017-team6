package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Model.Item;
import ch.ese.team6.Repository.ItemRepository;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping(path="/add")
	public String createForm(Model model) {
		// add an Attribute called 'item', and fill it with an empty Item-Object
		  model.addAttribute("item", new Item());
	        return "item/create";
	}

	
	@PostMapping(path="/add")
	public ModelAndView addNewItem(@ModelAttribute Item itemvalue) {
		
		Item item = new Item();
		item=itemvalue;
		itemRepository.save(item);
		return new ModelAndView("/item/show", "item", item);
	}
	
	@RequestMapping(path="/")
	public String showAllUsers(Model model) {
		model.addAttribute("items", itemRepository.findAll());
		return "item/index";
	}
	
	
}
