package ch.ese.team6.models.customers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name = "Customer")
public class Customer {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String name;

    private int addressId;

    public Customer(String customername) {
    	this.name = customername;
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
	
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	
	public int getAddressId() {
		return this.addressId;
	}


}


