package spring.mvc.home;

import java.security.Principal;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import spring.mvc.signup.SignupForm;
import spring.mvc.account.*;
@Controller
public class HomeController {
	@Autowired
	private AccountRepository accountRepository;
	
	//ten kontroler powinnien być jak najmniejszy!
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		new SignupForm();
		if(principal != null){
			model.addAttribute("name",principal.getName());
			model.addAttribute("schedule", accountRepository.userSchedule(principal.getName()));
			return "home/homeSignedIn";
		}

		return "home/homeNotSignedIn";
	}
}
