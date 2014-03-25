package spring.mvc.account;

import java.util.Collections;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import spring.mvc.schedule.Schedule;
import spring.mvc.schedule.ScheduleRepository;

public class UserService implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;
	@PostConstruct
	public void initialize() {
		Account acc1 = new Account("user", "demo", "ROLE_USER");
		Account acc2 = new Account("admin", "admin", "ROLE_ADMIN");
		Schedule sch1 = new Schedule("pierwsze zajecia", new Date(114,03,21,10,00));
		Schedule sch2 = new Schedule("drugie zajecia", new Date(114,03,23,11,00));
		Schedule sch3 = new Schedule("trzecie zajecia", new Date(114,03,26,12,00));	
		accountRepository.save(acc1);
		accountRepository.save(acc2);		
		sch1.setTrainer(acc1);
		sch2.setTrainer(acc2);
		sch3.setTrainer(acc2);
		scheduleRepository.save(sch1);
		scheduleRepository.save(sch2);
		scheduleRepository.save(sch3);
		accountRepository.addScheduleToSet(acc1, sch1);
		accountRepository.addScheduleToSet(acc2, sch2);	
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username);
		if (account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}

	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(
				authenticate(account));
	}

	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account),
				null, Collections.singleton(createAuthority(account)));
	}

	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(),
				Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

}
