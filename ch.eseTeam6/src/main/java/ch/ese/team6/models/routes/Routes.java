package ch.ese.team6.models.routes;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.tomcat.util.http.FastHttpDateFormat;
import org.springframework.beans.factory.annotation.Autowired;

import ch.ese.team6.models.trucks.TruckRepository;
import ch.ese.team6.models.trucks.Trucks;
import ch.ese.team6.models.users.UserRepository;
import ch.ese.team6.models.users.Users;

@Entity
public class Routes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull private Calendar routeDate;
	@NotNull private long vehicleId;
	@NotNull private long driverId;
	@NotNull private long deliveryId;
	
	@Autowired @Transient UserRepository userRepository; 
	@Transient @Autowired
	TruckRepository truckRepository; 
	
	
	public long getId() {
		return id;
	}
	
	public Routes(int vehicleId, int driverId, int deliveryId) {
		this.routeDate = Calendar.getInstance();
		this.vehicleId = vehicleId;
		this.driverId = driverId;
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

	public long getVehicleId() {
		return vehicleId;
	}
	
	public String getVehicleName() {
		List<Trucks> trucks = truckRepository.findAll();
		return "Hi";
	}

	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public long getDriverId() {
		return driverId;
	}
	
	public String getDriverName() {
		Users driver = userRepository.findOne(this.driverId);
		return driver.getRealName();	
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
