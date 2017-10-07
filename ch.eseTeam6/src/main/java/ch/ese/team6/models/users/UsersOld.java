package ch.ese.team6.models.users;

import java.util.Collections;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Entity // This tells Hibernate to make a table out of this class
public class UsersOld {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private User user;
    
    private String userName;
    
    private String realName;
    
    public UsersOld(int id, String userName, String realName, String password) {
    	this.userName = userName;
    	this.realName = realName;
    	
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	
	public String getRealName() {
		return this.realName;
	}
	public void setRealName(String name) {
		this.realName = name;
	}

	
}
