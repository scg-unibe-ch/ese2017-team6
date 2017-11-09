package ch.ese.team6.Model;

import java.util.ArrayList;
import java.util.Collection;


public class Delivery {
	
	
	private Address address;
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


	public Route getRoute() {
		return route;
	}


	public void setRoute(Route route) {
		this.route = route;
	}
	
	
}
