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

	
	/**
	 * A Delivery object consists of an adress and several orderitems
	 * The delivery is created by the route on the fly and never
	 * stored in the database.
	 * 
	 * The route will automatically join the orderitems with the same
	 * address to one delivery. 
	 * 
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
	
	public int getNumberOfItems() {
		return this.items.size();
	}
	
	
	public Route getRoute() {
		return route;
	}


	public void setRoute(Route route) {
		this.route = route;
	}
	
	/**
	 * returns the whole weight of the delivery
	 * @return
	 */
	public int getWeight() {
		int sum = 0;
		
		for(OrderItem oi : this.items)
			{sum += oi.getWeight();}
		
		return sum;
	}
	
	/**
	 * returns the whole size of the delivery
	 * @return
	 */
	public int getSize() {
		int sum = 0;
		
		for(OrderItem oi : this.items)
			{sum += oi.getSize();}
		
		return sum;
	}


	public String getDistanceToNextDeliveryStr() {
		if(distanceToNextDelivery==0) {
			return "";
		}
		return "drive "+CalendarService.formatMinutes(distanceToNextDelivery);
	}


	
	/**
	 * This parameter will only be used to display the distance to the next
	 * delivery. It is set by the route when it constructs the deliveries
	 * @param distanceToNextDelivery
	 */
	public void setDistanceToNextDelivery(long distanceToNextDelivery) {
		this.distanceToNextDelivery = distanceToNextDelivery;
	}


	public String getDistanceFromPreviousDeliveryStr() {
		if(distanceFromPreviousDelivery==0) {
			return "";
		}
		return "drive "+CalendarService.formatMinutes(distanceFromPreviousDelivery);
	}

	
	/**
	 * This distance will only be used for display purposes
	 * namely when you call getDistanceFromPreviousDeliveryStr
	 * This is set by the route
	 * @param distanceFromPreviousDelivery
	 */
	public void setDistanceFromPreviousDelivery(long distanceFromPreviousDelivery) {
		this.distanceFromPreviousDelivery = distanceFromPreviousDelivery;
	}
	
	
}
