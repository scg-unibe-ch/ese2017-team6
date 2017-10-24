package ch.ese.team6.models.customers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Address {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	
	@NotNull
	private String street;
	//private String houseNumber; //has to be a String. Hauptstrasse 11a
	@NotNull
	private String city; //and Poste Code
	
	public Address() {
		
	}
	
	public Address(String street, String city) {
	
		this.street = street;
		this.city = city;
	}

	/**
	 * Returns the distance in km to reach otherAddress starting
	 * at this Address.
	 * The function does not have to be symmetric (i. e. A.getDistanceTo(B) does not have to 
	 * be the same as B.getDistanceTo(A) 
	 * @param otherAddress
	 * @return distance in km
	 */


	public int getDistanceTo(Address otherAddress) {
		//dummy
		return 1;
	}
	
	public long getId() {
		return this.id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public String toString() {
		return street+", "+city;
	}
	
	private boolean invariant() {
		return (this.street!=null && this.city != null && this.street.length()!=0 && this.city.length()!=0);
	}

	public boolean isOK() {
		return this.invariant();
	}

}
