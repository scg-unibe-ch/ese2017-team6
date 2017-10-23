package ch.ese.team6.models.users;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "user")
public class Users {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String username;
	private String realName;
	private String password;
	private String passwordConfirm;
	private Set<Role> roles;
	
	
	public Users(String username, String realName,String password, int userrole) {
		this.username=username;
		this.realName = realName;
		this.password = password;
	}
	
	public Users(String username, String realName,String password) {
		this.username=username;
		this.realName = realName;
		this.password = password;
	}
	
	
	public Users() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String email) {
		this.realName = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	@Transient
	public String getPasswordConfirm() {
		return this.passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = 
	@JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	public Set <Role> getRoles(){
		return this.roles;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String toString() {
		return this.realName;
	}
	
}