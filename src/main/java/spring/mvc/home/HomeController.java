package spring.mvc.home;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
	//ten kontroler powinnien być jak najmniejszy!
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal) {
		return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
	}
}
