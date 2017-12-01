package ch.ese.team6.Model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import ch.ese.team6.Service.CalendarService;

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
	private DataStatus status;
	
	@OneToMany(mappedBy="origin")
	private Set<Distance> outGoing;
	
	@OneToMany(mappedBy="destination")
	private Set<Distance> inComing;
	
	
	public Address() {
		this.status = DataStatus.ACTIVE;
	}
	
	public Address(String street, String city) {
	
		this.street = street;
		this.city = city;
		this.status = DataStatus.ACTIVE;
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

	@Override
	public String toString() {
		return street+", "+city+", "+country;
	}
	
	public String getStreetandCity() {
		return street+", "+city;
	}
	
	public DataStatus getStatus() {
		return this.status;			
	}
			
	public void setStatus(DataStatus status) {
		this.status = status;
	}
	
	private boolean invariant() {
		return (this.street!=null && this.city != null && this.street.length()!=0 && this.city.length()!=0 && this.country!= null && this.country.length()!=0);
	}

	public boolean isOK() {
		return this.invariant();
	}

	public boolean equals(Address address) {
		// TODO Auto-generated method stub
		if(address.getId() == this.id)
			return true;
		else
			return false;
	}
	/**
	 * Returns the minutes it takes to drive from Address 1 to Address 2
	 * Returns 0 if there is not distance object which can answer the question
	 * This function is symmetric. It is guaranteed that this.getDistance(other) is the same as other.getDistance(this)
	 * @param address2
	 * @return
	 */
	public long getDistance(Address address2) {
		long minutes = 0;
		int datapoints = 0;
		for(Distance distance:this.outGoing) {
			if(distance.getOrigin().getId()==this.getId()) {
				if(distance.getDestination().getId()==address2.getId()) {
					minutes+= (distance.getDurationSeconds()/60l);
					datapoints++;
					break;
				}
				
			}
		}
		
		for(Distance distance:this.inComing) {
			if(distance.getOrigin().getId()==address2.getId()) {
				if(distance.getDestination().getId()==this.getId()) {
					minutes+= (distance.getDurationSeconds()/60l);
					datapoints++;
					break;
				}
				
			}
		}
		
		/*
		 * We return the average of the incoming and outgoing distance if possible
		 */
		if(datapoints>0) {
			return minutes/datapoints;
		}
		return 0;
	}
/*
	public List<Distance> getDistances() {
		// TODO Auto-generated method stub
		return null;
	}
*/

	public String getDistanceStr(Address nextAddress) {
		return CalendarService.formatMinutes(this.getDistance(nextAddress));
	}
}
