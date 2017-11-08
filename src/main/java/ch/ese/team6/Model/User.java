package ch.ese.team6.Model;
import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    private Long id;
    private String username;
    private String password;
    private String passwordConfirm;
    private Set<Role> roles;
    
    private String firstname;
    private String surname;
    private String email;
    private String phoneNumber;
    
    private List<Route> routes;
    
    
    public User() {
		// TODO Auto-generated constructor stub
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getRealname() {
    	return this.firstname+" "+this.surname;
    }
    
    public void setRealname(String name) {
    	String[] names = name.split(" ");
    	this.firstname = names[0];
    	this.surname = names[1];
    }
    @Override
    public String toString() {
    	return this.getRealname();
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@OneToMany(mappedBy="driver")
	public List<Route> getRoutes(){
		try {
			return this.routes;
		}
		catch(Exception exception) {
			return null;
		}
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public boolean isOccupied(String date) {
		for(Route route: this.routes) {
			if (route.getRouteDate()== date) return true;
		}
		return false;
	}
}