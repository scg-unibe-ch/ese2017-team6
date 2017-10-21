package ch.ese.team6.models.trucks;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;

@Entity
public class Trucks {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull private String vehicleName;
	@NotNull private int maxCargoSpace;
	@NotNull private int maxLoadCapacity;
	@NotNull private int vehicleCondition;
	
	public Trucks(String name, int maxCargoSpace,int maxLoadCapacity, int vehicleCondition) {
		this.vehicleName=name;
		this.maxCargoSpace = maxCargoSpace;
		this.maxLoadCapacity = maxLoadCapacity;
		this.vehicleCondition = vehicleCondition;
	}
		
	public Trucks() {
		// TODO Auto-generated constructor stub
	}

	public Trucks(String vehicleName) {
		this.vehicleName = vehicleName;
		this.maxCargoSpace = 60;
		this.maxLoadCapacity = 40;
		this.vehicleCondition = 0;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
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
	
	public long getId() {
		return id;
	}
	
	public String toString() {
		return this.vehicleName;
	}
	

	
}
