package ch.ese.team6.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	public Truck() {}
	
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
	
}
