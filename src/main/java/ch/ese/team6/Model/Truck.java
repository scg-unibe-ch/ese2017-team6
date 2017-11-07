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
	private int maxSize;
	private int maxWeight;
	private int vehicleCondition;
	
	public Truck() {}
	
	public Truck(String name) {
		this.truckname = name;
		this.maxSize = this.maxWeight = 40;
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
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxCargoSpace) {
		this.maxSize = maxCargoSpace;
	}
	public int getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(int maxLoadCapacity) {
		this.maxWeight = maxLoadCapacity;
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
	
	@Override
	public String toString() {
		return this.getTruckname();
	}
	
}
