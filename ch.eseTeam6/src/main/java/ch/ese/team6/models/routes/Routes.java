package ch.ese.team6.models.routes;


import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;

@Entity
public class Routes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotNull private Date routeDate;
	@NotNull private int vehicleId;
	@NotNull private int driverId;
	@NotNull private int deliveryId;
	
	public long getId() {
		return id;
	}
	
	public Routes(Date routeDate, int vehicleId, int driverId, int deliveryId) {
		this.routeDate = routeDate;
		this.vehicleId = vehicleId;
		this.driverId = driverId;
		this.deliveryId = deliveryId;
	}
		
	public Routes() {
		// TODO Auto-generated constructor stub
	}

	public Date getRouteDate() {
		return routeDate;
	}

	public void setRouteDate(Date routeDate) {
		this.routeDate = routeDate;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public int getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
