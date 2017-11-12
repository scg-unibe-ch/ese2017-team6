package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import ch.ese.team6.Exception.BadSizeException;

@Entity 
@Table(name = "Customer")
public class Customer {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	@NotNull
    private String name;
	@NotNull
	private String phone;
	@NotNull
	private String email;
	@NotNull @Min(value = 0) @Max( value = 1)
	private int customerStatus;
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADDRESS_ID")
	@NotNull
    private Address address;

    public Customer(String customername, Address address) {
    	this.name = customername;
    	this.address = address;
    }
    public Customer() {
    	this.customerStatus = 0;
    }
    
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getCustomerStatus() {
		return customerStatus;
	}
	
	public String getCustomerStatusAsString() {
		if (this.getCustomerStatus() == 0)
				return "active";
		return "inactive";
	}
	
	public void setCustomerStatus(int customerStatus) throws BadSizeException {
		if(!(customerStatus == 0  || customerStatus == 1)) throw new BadSizeException("customer status must be 1 or 0");
		this.customerStatus = customerStatus;
	}

	public void setId(long id) {
		this.id = id;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String toString() {
		return name;
	}
	public boolean isOK() {
		return this.invariant();
	}
	private boolean invariant() {
		return name!=null && address!=null &&name.length()!=0;
	}


}


