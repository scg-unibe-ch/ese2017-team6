package ch.ese.team6.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticPagesController {
	
	@RequestMapping(path ="/")
	public String showHome() {
		return "staticpages/home";
	}

}
