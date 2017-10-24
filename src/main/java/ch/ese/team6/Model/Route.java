package ch.ese.team6.Model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "route")
public class Route {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long id;
	public Date routeDate;
	public String truckName;
	public String driverName;
	public String addresses;
	
	public Route() {}
	
	public Route(boolean test) {
		if(test) {
			this.routeDate.setTime(System.currentTimeMillis());
			this.truckName = "Lastwagen";
			this.driverName= "Fahrlehrer";
			addresses = ("Heinrich Heine Dorfbach 555, 3000 Bern");
		}
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getRouteDate() {
		return routeDate;
	}

	public void setRouteDate(Date routeDate) {
		this.routeDate = routeDate;
	}

	public String getTruckName() {
		return truckName;
	}

	public void setTruckName(String truckName) {
		this.truckName = truckName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}
	
}
