package spring.mvc.schedule;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import spring.mvc.account.Account;

@SuppressWarnings("serial")
@Entity
@Table(name = "Schedule")
public class Schedule implements java.io.Serializable {

	@Id
	@Column(name = "ScheduleId")
	@GeneratedValue
	private Long id;

	@ManyToMany(mappedBy = "schedules")
	private Set<Account> accounts = new HashSet<Account>();

	@Column
	private String className;

	@Column
	private Date classDate;
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="AccountId")
	public Account trainer;
	
	protected Schedule() {
	}

	public Account getTrainer() {
		return trainer;
	}

	public void setTrainer(Account trainer) {
		this.trainer = trainer;
	}
	
	public Schedule(String ClassName, Date date) {
		this.className = ClassName;
		this.classDate = date;
	}

	public Long getId() {
		return id;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Date getClassDate() {
		return classDate;
	}

	public void setClassDate(Date classDate) {
		this.classDate = classDate;
	}
}
