package ch.ese.team6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Model.Item;
import ch.ese.team6.Model.DataStatus;
import ch.ese.team6.Model.Truck;
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
		return new ModelAndView("item/show", "item", item);
	}
	
	@RequestMapping(path="/")
	public String showAllUsers(Model model) {
		model.addAttribute("items", itemRepository.findAll());
		return "item/index";
	}
	
	@GetMapping(path = "/{itemId}")
	public String showItem(Model item,@PathVariable Long itemId) {
		item.addAttribute("item", itemRepository.findOne(itemId));
		return "item/profile"; 
	}
	
	@GetMapping(path = "/{itemId}/edit")
	public String editItem(Model item, @PathVariable long itemId) {
		item.addAttribute("item", itemRepository.findOne(itemId));
		item.addAttribute("statusArray", DataStatus.values());
		return "item/edit";
	}
	
	@PostMapping(path = "/{itemId}/edit")
	public String editItem (Model model, @ModelAttribute Item itemvalue, @PathVariable long itemId) {
		Item item = itemRepository.findOne(itemId);
		try {
			item.setName(itemvalue.getName());
			item.setStatus(itemvalue.getStatus());
		} catch (BadSizeException e) {
			// TODO Auto-generated catch block
			return "staticpage/index";
		}
		itemRepository.save(item);
		model.addAttribute("item", item);
		return ("item/profile");
	}	
}
