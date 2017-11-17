package ch.ese.team6.Model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ch.ese.team6.Exception.BadSizeException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
@Entity
@Table(name= "truck")
public class Truck {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NotNull @Column(name = "truckname", unique = true)
	private String truckname;
	@NotNull @Min(value = 0)
	private int maxCargoSpace;
	@NotNull @Min( value = 0)
	private int maxLoadCapacity;
	//@NotNull @Min(value = 0) @Max( value = 1)
	private DataStatus status;
	@OneToMany(mappedBy="truck")
	private List<Route> routes;
	
	public Truck() {
 		this.status = DataStatus.ACTIVE;
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
	
	public void setTruckname(String truckname) throws BadSizeException{
		truckname.trim();
		if (truckname.isEmpty()) throw new BadSizeException ("Truckname can't be empty.");
		this.truckname = truckname;
	}
	public int getMaxCargoSpace() {
		return maxCargoSpace;
	}
	public void setMaxCargoSpace(int maxCargoSpace) throws BadSizeException {
		if(maxCargoSpace == 0) throw new BadSizeException ("Max cargo space can't be null.");
		if(maxCargoSpace < 0) throw new BadSizeException("Max cargo space can't be negative.");
		this.maxCargoSpace = maxCargoSpace;
	}
	public int getMaxLoadCapacity() {
		return maxLoadCapacity;
	}
	public void setMaxLoadCapacity(int maxLoadCapacity) throws BadSizeException {
		if(maxLoadCapacity == 0) throw new BadSizeException ("Max load capacity can't be null.");
		if(maxLoadCapacity < 0) throw new BadSizeException("Max load capacity can't be negative.");
		this.maxLoadCapacity = maxLoadCapacity;
	}
	
	public DataStatus getStatus() {
		return this.status;			
	}
			
	public void setStatus(DataStatus status) {
		this.status = status;
	}
//	public int getVehicleCondition() {
//		return vehicleCondition;
//	}
//	
//	public String getVehicleConditionAsString() {
//		if (this.getVehicleCondition() == 0) return "active";
//		return "inactive";
//	}
//	
//	public void setVehicleCondition(int vehicleCondition) throws BadSizeException{
//		if(!(vehicleCondition == 0 || vehicleCondition == 1)) throw new BadSizeException("Vehicle conditions has to be one or null");
//		this.vehicleCondition = vehicleCondition;
//	}
	
	public boolean isValid() {
		if(
			this.truckname.isEmpty() ||
//			!((this.vehicleCondition )== 0 || (this.vehicleCondition == 1)) ||
			!(this.maxCargoSpace > 0) ||
			!(this.maxLoadCapacity > 0)) {return false;}
		return true;
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

	public boolean hasId() {
		if (this.id != 0) return true;
		return false;
	}
	
}
