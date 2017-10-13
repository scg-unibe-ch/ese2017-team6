package ch.ese.team6.models.clients;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

@Entity
@Table(name = "client")
public class Client {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    private int addressId;
    
    public Client(String name) {
    	this.name = name;
    }

	public Integer getId() {
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


