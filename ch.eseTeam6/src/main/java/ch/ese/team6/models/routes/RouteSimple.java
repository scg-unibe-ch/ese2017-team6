package ch.ese.team6.models.routes;

import java.util.Date;

public class RouteSimple {

	private Date routeDate;
	private long driverId;
	private long truckId;
	
	public RouteSimple(Date routeDate) {
		this.routeDate = routeDate;
	}
	
	public RouteSimple() {}
	
	public Date getDate() {
		return this.routeDate;
	}
		
	public void setDate(Date routeDate) {
		this.routeDate = routeDate;
	}
	
	public long getDriverId() {
		return this.driverId;
	}
	
	public long getTruckId() {
		return this.truckId;
	}
}
