package ch.ese.team6.Model;

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
	@NotNull
	private String country;
	
	public Address() {
		
	}
	
	public Address(String street, String city) {
	
		this.street = street;
		this.city = city;
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
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String toString() {
		return street+", "+city;
	}
	
	private boolean invariant() {
		return (this.street!=null && this.city != null && this.street.length()!=0 && this.city.length()!=0 && this.country!= null && this.country.length()!=0);
	}

	public boolean isOK() {
		return this.invariant();
	}

}
