package ch.ese.team6.models.routes;



import java.text.SimpleDateFormat;
import java.util.Calendar;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;


import ch.ese.team6.models.trucks.Trucks;
import ch.ese.team6.models.users.Users;


@Entity
public class Routes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private Calendar routeDate;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TRUCK_ID")
	private Trucks truck;
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DRIVER_ID")
	private Users driver;
	
	private long deliveryId;
	
	
	public long getId() {
		return id;
	}
	
	public Routes(Trucks truck, Users driver, int deliveryId) {
		this.routeDate = Calendar.getInstance();
		this.truck = truck;
		this.driver = driver;
		this.deliveryId = deliveryId;
	}
	
	public Routes() {
		// TODO Auto-generated constructor stub
	}

	public String getRouteDate() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd");
		return date.format(routeDate.getTime());
	}

	public void setRouteDate(Calendar routeDate) {
		this.routeDate = routeDate;
	}

	public void setTruck(Trucks truck) {
		this.truck = truck;
		
	}
		
	public String getTruckName() {
		return this.truck.getVehicleName();
	}

	public String getDriverName() {
		return this.driver.getRealName();
	}
	
	public long getDriverId() {
		return this.driver.getId();
	}
	
	public void setDriver(Users driver) {
		this.driver = driver;
	}

	public long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(long i) {
		this.deliveryId = i;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
}
