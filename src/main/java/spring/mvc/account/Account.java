package spring.mvc.account;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import spring.mvc.schedule.Schedule;

@SuppressWarnings("serial")
@Entity
@Table(name = "Account")
@NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where a.email = :email")
public class Account implements java.io.Serializable {

	public static final String FIND_BY_EMAIL = "Account.findByEmail";

	@Id
	@Column(name = "AccountId")
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String email;

	@Column
	private String password;

	@Column
	private String role = "ROLE_USER";

	@ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "Account_Schedule", joinColumns = { @JoinColumn(name = "AccountId") }, inverseJoinColumns = { @JoinColumn(name = "ScheduleId") })
	private Set<Schedule> schedules = new HashSet<Schedule>();

	
	@OneToMany(mappedBy="trainer", cascade={CascadeType.ALL})
    private Set<Schedule> trainerSchedules;
	
	protected Account() {

	}

	public Account(String email, String password, String role) {
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setSchedules(Set<Schedule> schedules) {
		this.schedules = schedules;
	}
	
	public Set<Schedule> getSchedules() {
		return schedules;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
