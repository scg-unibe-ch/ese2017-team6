package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.Collection;

import ch.ese.team6.Service.CalendarService;


public class Delivery {
	
	
	private Address address;
	
	private long distanceToNextDelivery;
	private long distanceFromPreviousDelivery;
	private Route route;
	
	private ArrayList<OrderItem> items;
	
	/*
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="route")
	private Route route;
	*/
	
	public Delivery() {}


	public Delivery(Route route) {
		this.setRoute(route);
		this.items = new ArrayList<OrderItem>();
	}



	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}
	
	


	public ArrayList<OrderItem> getItems() {
		return items;
	}
	


	public void addItem(OrderItem item) {
		this.items.add(item);
	}
	public void addItems(Collection<? extends OrderItem> c) {
		this.items.addAll(c);
	}
	
	public boolean hasItems(){
		return !this.items.isEmpty();
		
	}
	
	public int getSize() {
		return this.items.size();
	}
	
	


	public Route getRoute() {
		return route;
	}


	public void setRoute(Route route) {
		this.route = route;
	}


	public String getDistanceToNextDeliveryStr() {
		if(distanceToNextDelivery==0) {
			return "";
		}
		return "drive "+CalendarService.formatMinutes(distanceToNextDelivery);
	}


	public void setDistanceToNextDelivery(long distanceToNextDelivery) {
		this.distanceToNextDelivery = distanceToNextDelivery;
	}


	public String getDistanceFromPreviousDeliveryStr() {
		if(distanceFromPreviousDelivery==0) {
			return "";
		}
		return "drive "+CalendarService.formatMinutes(distanceFromPreviousDelivery);
	}


	public void setDistanceFromPreviousDelivery(long distanceFromPreviousDelivery) {
		this.distanceFromPreviousDelivery = distanceFromPreviousDelivery;
	}
	
	
}
