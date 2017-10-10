package ch.ese.team6.models.users;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;

@Entity
public class Users {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull private String username;
	@NotNull private String realName;
	@NotNull private String password;
	@NotNull private int userrole;
	
	public long getId() {
		return id;
	}
	
	public Users(String username, String realName,String password, int userrole) {
		this.username=username;
		this.realName = realName;
		this.password = password;
		this.userrole = userrole;
	}
	
	public Users(String username, String realName,String password) {
		this.username=username;
		this.realName = realName;
		this.password = password;
	}
	
	
	public Users() {
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return password;
	}

	public int getUserrole() {
		return userrole;
	}
	
}
