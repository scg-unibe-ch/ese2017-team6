package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	private String street;
	private int houseNumber;
	private String city;
	
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(int streetNumber) {
		this.houseNumber = streetNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public String toSring() {
		return street+", "+houseNumber+"; "+city;
	}
}
