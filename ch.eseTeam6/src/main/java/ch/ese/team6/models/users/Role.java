package ch.ese.team6.models.users;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name ="role")
public class Role {
	private long id;
	private String name;
	private Set<Users> users;
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany(mappedBy= "roles")
	public Set<Users> getUsers(){
		return users;
	}
	
	public void setUsers(Set<Users> users) {
		this.users =users;
	}
}

