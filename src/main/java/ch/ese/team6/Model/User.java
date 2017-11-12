package ch.ese.team6.Model;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import ch.ese.team6.Exception.BadSizeException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table( name = "ese_user")
public class User {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	@NotNull @Size(min = 6, max = 20, 
			message = "username must be between 6 and 20 signs long.")
	@Column(name = "username", unique = true)
	private String username;
	@NotNull
    private String password;
	@Transient
    private String passwordConfirm;
	@ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
    		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
    @NotNull
    private String firstname;
    @NotNull
    private String surname;
    @NotNull @Email
    private String email;
    private String phoneNumber;
	@NotNull @Min(value = 0) @Max( value = 1)
	private int userCondition;
    @OneToMany(mappedBy="driver")
    private List<Route> routes;
    
    @Transient
    public static final String EMAIL_VERIFICATION = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    
    
    public User() {
		this.userCondition = 0;
		// TODO Auto-generated constructor stub
	}

	
    public Long getId() {
    	try {
        return id;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return (long) 0;
    	}
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws BadSizeException{
    	username = username.trim();
    	if ((username.length() >= 6 && username.length() <= 32)) {this.username = username;}
    	else {throw new BadSizeException("username should be between 6 and 32 chars long!");}
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws BadSizeException {
    	password.trim();
        if (password.trim().isEmpty()) throw new BadSizeException("password can't be empty");
    	this.password = password;
    }

    
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) throws BadSizeException {
    	passwordConfirm.trim();
    	if (passwordConfirm.trim().isEmpty()) throw new BadSizeException("password can't be empty");
    	this.passwordConfirm = passwordConfirm;
    }

    
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) throws BadSizeException{
    	if (roles.isEmpty()) throw new BadSizeException("user needs a role");
        this.roles = roles;
    }
    
    public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) throws BadSizeException {
		firstname = firstname.trim();
		if (firstname.isEmpty()) throw new BadSizeException("firstname can't be empty");
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) throws BadSizeException{
		surname = surname.trim();
		if (surname.isEmpty()) throw new BadSizeException("surname can't be empty");
		this.surname = surname;
	}

	public String getRealname() {
    	return this.firstname+" "+this.surname;
    }
    
    @Override
    public String toString() {
    	return this.getRealname();
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws BadSizeException{
		email.trim();
		if (!email.matches(EMAIL_VERIFICATION)) throw new BadSizeException("Email has not the correct format.");
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) throws BadSizeException{
		phoneNumber.trim();
		if (phoneNumber.isEmpty()) throw new BadSizeException("phoneNumber can't be empty!.");
		this.phoneNumber = phoneNumber;
	}
	
	public int getUserCondition() {
		return this.userCondition;
	}
	
	public String getUserConditionAsString() {
		if (this.getUserCondition() == 0) return "active";
		return "inactive";
	}
	
	public void setUserCondition(int userCondition) throws BadSizeException{
		if(!(userCondition == 0 || userCondition == 1)) throw new BadSizeException("user conditions has to be one or null");
		this.userCondition = userCondition;
	}
	
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
	
	public boolean isValid() {
		try{
			if(
			this.username.isEmpty() ||
			!(this.password == this.passwordConfirm) ||
			this.firstname.isEmpty() ||
			this.surname.isEmpty() ||
			this.email.isEmpty() ||
			this.phoneNumber.isEmpty() ||
			!((this.userCondition )== 0 || (this.userCondition == 1)) ||
			this.roles.isEmpty()
			) return false;
			}
		catch(NullPointerException n) {
			return false;
		}
		return true;
	}


	public void setRole(Role role) throws BadSizeException {
		Set<Role> roles = new HashSet<Role>();
		roles.add(role);
		this.setRoles(roles);
	}


	public boolean hasId() {
		try {
			long i = this.id;
		}
		catch(NullPointerException e) {
			return false;
		}
		return true;
	}
}