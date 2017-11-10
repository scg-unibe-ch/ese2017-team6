package ch.ese.team6.Model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name= "truck")
public class Truck {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String truckname;
	private int maxCargoSpace;
	private int maxLoadCapacity;
	private int vehicleCondition;
	@OneToMany(mappedBy="truck")
	private List<Route> routes;
	
	public Truck() {}
	
	public Truck(String name) {
		this.truckname = name;
		this.maxCargoSpace = this.maxLoadCapacity = 40;
		this.vehicleCondition = 0;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTruckname() {
		return truckname;
	}
	public void setTruckname(String truckname) {
		this.truckname = truckname;
	}
	public int getMaxCargoSpace() {
		return maxCargoSpace;
	}
	public void setMaxCargoSpace(int maxCargoSpace) {
		this.maxCargoSpace = maxCargoSpace;
	}
	public int getMaxLoadCapacity() {
		return maxLoadCapacity;
	}
	public void setMaxLoadCapacity(int maxLoadCapacity) {
		this.maxLoadCapacity = maxLoadCapacity;
	}
	public int getVehicleCondition() {
		return vehicleCondition;
	}
	
	public String getVehicleConditionAsString() {
		if (this.getVehicleCondition() == 0) return "active";
		return "inactive";
	}
	
	public void setVehicleCondition(int vehicleCondition) {
		this.vehicleCondition = vehicleCondition;
	}
	
	public List<Route> getRoutes(){
		try {
			return this.routes;
		}
		catch(Exception exception) {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return this.getTruckname();
	}

	public boolean isOccupied(String date) {
		for(Route route: this.routes) {
			if (route.getRouteDate()== date) return true;
		}
		return false;
	}
	
}
