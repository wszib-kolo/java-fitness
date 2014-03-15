package spring.mvc.home;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.mvc.signup.SignupForm;
@Controller
public class HomeController {
	//ten kontroler powinnien być jak najmniejszy!
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		new SignupForm();
		if(principal != null){
			model.addAttribute("name",principal.getName());
			return "home/homeSignedIn";
		}

		return "home/homeNotSignedIn";
	}
}
