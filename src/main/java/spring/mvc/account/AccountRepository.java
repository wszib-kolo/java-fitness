package spring.mvc.account;

import java.util.List;
import java.util.logging.LogManager;

import javax.persistence.*;
import javax.persistence.criteria.From;
import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mysql.jdbc.log.Log;

import spring.mvc.account.Account;


@Repository
@Transactional(readOnly = true)
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
		sessionFactory.getCurrentSession().persist(account);
		return account;
	}

	public Account findByEmail(String email) {
		try {
			List<Account> acc = sessionFactory.getCurrentSession().createQuery("From Account Where email = :email").setParameter("email", email).list();
			return acc.get(0);
		} catch (PersistenceException e) {
			return null;
		}
	}

}
