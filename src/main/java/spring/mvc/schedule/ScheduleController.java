package spring.mvc.schedule;

import java.security.Principal;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import spring.mvc.account.Account;
import spring.mvc.account.AccountRepository;
@Controller
public class ScheduleController {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@RequestMapping(value = "schedule", method = RequestMethod.GET)
	public String index(Principal principal, Model model) {
		if(principal != null){ 
			
			model.addAttribute("name",principal.getName());
			model.addAttribute("scheduleSignForm", new ScheduleSignForm());
			model.addAttribute("schedule", scheduleRepository.notSignedUpSchedule(principal.getName()));
			return "schedule/schedule";
		}
		return "/";
	}
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@RequestMapping(value = "schedule", method = RequestMethod.POST)
	public String saveToClass(Principal principal, Model model, @ModelAttribute("scheduleSignForm") ScheduleSignForm signinForm) {

		if(principal != null){
			Schedule sch = scheduleRepository.findById(signinForm.getScheduleId());
			Account acc = accountRepository.findByEmail(principal.getName());
			acc.getSchedule().add(sch);
			accountRepository.save(acc);
			model.addAttribute("name",principal.getName());
			model.addAttribute("scheduleSignForm", new ScheduleSignForm());
			model.addAttribute("schedule", scheduleRepository.notSignedUpSchedule(principal.getName()));
			return "schedule/schedule";
		}
		return "schedule/scheduleSign";
	}
}
