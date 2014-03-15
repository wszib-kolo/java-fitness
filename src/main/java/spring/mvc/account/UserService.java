package spring.mvc.account;

import java.util.Collections;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;

import spring.mvc.schedule.*;

public class UserService implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@PostConstruct
	public void initialize() {
		Account acc1 = new Account("user", "demo", "ROLE_USER");
		Account acc2 = new Account("admin", "admin", "ROLE_ADMIN");
		Schedule sch1 = new Schedule("jakies zajecia", new Date(100342));
		Schedule sch2 = new Schedule("inne zajecia", new Date(104542));
		acc1.getSchedule().add(sch1);
		acc1.getSchedule().add(sch2);
		acc2.getSchedule().add(sch2);
		accountRepository.save(acc1);
		accountRepository.save(acc2);
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
