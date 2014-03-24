package spring.mvc.schedule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import spring.mvc.account.Account;

@Repository
@Transactional(readOnly = false)
public class ScheduleRepository {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public Schedule save(Schedule schedule) {
		sessionFactory.getCurrentSession().saveOrUpdate(schedule);
		return schedule;
	}
	
	@Transactional
	public List<Schedule> allSchedule(){
		@SuppressWarnings("unchecked")
		List<Schedule> schedule = sessionFactory.getCurrentSession().createQuery("From Schedule").list();
		return schedule;
	}
	
	public Schedule findById(long scheduleId) {
		try {
			Schedule schedule = (Schedule) sessionFactory.getCurrentSession()
					.createQuery("From Schedule Where id = :id")
					.setParameter("id", scheduleId).uniqueResult();
			return schedule;
		} catch (PersistenceException e) {
			return null;
		}
	}
	
	public List<Schedule> notSignedUpSchedule(String email){
		List<Schedule> notSignUpSchedules = new ArrayList<Schedule>();
		List<Schedule> schedules = allSchedule();
		
		Iterator<Schedule> iterator = schedules.iterator();
		while (iterator.hasNext()) {
			boolean isSignUp = false;
			Schedule schedule = iterator.next();
			
			List <Account> accounts = schedule.getAccounts();
			Iterator<Account> account = accounts.iterator();
			while (account.hasNext()) {
				if(account.next().getEmail().compareTo(email)==0){
					isSignUp = true;
				}
			}
			if(isSignUp==false){
				notSignUpSchedules.add(schedule);
			}
		}
		return notSignUpSchedules;
	}
}
