package ch.ese.team6.models.customers;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity 
@Table(name = "Customer")
public class Customer {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String name;

  
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADDRESS_ID")
	@NotNull
    private Address address;

    public Customer(String customername, Address address) {
    	this.name = customername;
    	this.address = address;
    }
    public Customer() {
    	
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
	
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String toString() {
		return name;
	}


}


