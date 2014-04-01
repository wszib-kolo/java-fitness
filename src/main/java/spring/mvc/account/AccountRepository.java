package spring.mvc.account;

import java.util.HashSet;
import java.util.Iterator;
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
import spring.mvc.schedule.ScheduleRepository;

@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public class AccountRepository {

	// @PersistenceContext
	// private EntityManager entityManager;
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ScheduleRepository scheduleRepository;
	@Inject
	private PasswordEncoder passwordEncoder;

	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		sessionFactory.getCurrentSession().saveOrUpdate(account);
		return account;
	}

	@Transactional
	public Account addScheduleToSet(Account account, Schedule schedule) {
		if(schedule.incSignedUpUsersNumber() == false){
			return null;
		}
		Account acc = (Account) sessionFactory.getCurrentSession().get(Account.class, account.getId());
		if (acc == null) {
			acc = account;
		}
		Set<Schedule> sch = account.getSchedules();
		Iterator<Schedule> it = sch.iterator();
		
		Set<Schedule> newScheduleSet = new HashSet<Schedule>();
		while (it.hasNext()) {
			long scheduleId = it.next().getId();
			Schedule oldSchedule = (Schedule) sessionFactory.getCurrentSession().get(Schedule.class, scheduleId);
			if (oldSchedule == null) {
				oldSchedule = scheduleRepository.findById(scheduleId);
			}
			newScheduleSet.add(oldSchedule);
		}
		
		Schedule newSchedule = (Schedule) sessionFactory.getCurrentSession().get(Schedule.class, schedule.getId());
		if (newSchedule == null) {
			newSchedule = scheduleRepository.findById(schedule.getId());
		}
		newSchedule.incSignedUpUsersNumber();
		newScheduleSet.add(newSchedule);
		
		acc.setSchedules(newScheduleSet);
		sessionFactory.getCurrentSession().saveOrUpdate(acc);
		return account;
	}

	@Transactional
	public Set<Schedule> userSchedule(String email) {
		Account acc = (Account) sessionFactory.getCurrentSession().createQuery("From Account Where email = :email")
				.setParameter("email", email).uniqueResult();
		return acc.getSchedules();
	}

	public Account findByEmail(String email) {
		try {
			Account acc = (Account) sessionFactory.getCurrentSession().createQuery("From Account Where email = :email")
					.setParameter("email", email).uniqueResult();
			return acc;
		} catch (PersistenceException e) {
			return null;
		}
	}

}
