package spring.mvc.schedule;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public class ScheduleRepository {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public Schedule save(Schedule schedule) {
		sessionFactory.getCurrentSession().save(schedule);
		return schedule;
	}
}
