package spring.mvc.account;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import spring.mvc.schedule.Schedule;

@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public class AccountRepository {

	// @PersistenceContext
	// private EntityManager entityManager;
	@Autowired
	private SessionFactory sessionFactory;
	@Inject
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		sessionFactory.getCurrentSession().saveOrUpdate(account);
		return account;
	}

	@Transactional
	public Set<Schedule> userSchedule(String email){
		Account acc = (Account) sessionFactory.getCurrentSession()
				.createQuery("From Account Where email = :email")
				.setParameter("email", email).uniqueResult();
		return acc.getSchedule();
	}
	
	public Account findByEmail(String email) {
		try {
			Account acc = (Account) sessionFactory.getCurrentSession()
					.createQuery("From Account Where email = :email")
					.setParameter("email", email).uniqueResult();
			return acc;
		} catch (PersistenceException e) {
			return null;
		}
	}

}
